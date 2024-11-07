package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import jakarta.servlet.http.HttpSession;

import com.example.demo.otherfunction.encryption;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private customerrepository customerrepo;

    @Autowired
    private categoriesrepository caterepo;

    @Autowired
    private productrepository productrepo;

    @Autowired
    private productimagesrepository productimagerepo;

    @Autowired
    private cartrepository cartrepo;

    @GetMapping("index")
    public String index() {
        return "user/index";
    }

    @GetMapping("register")
    public String register(Model model) {
        customers customer = new customers();
        // Get the next available ProductId
        int nextCustomerId = customerrepo.findNextCustomerId();
        customer.setCustomerId(nextCustomerId);
        model.addAttribute("customers", customer);
        return "user/register";
    }

    @PostMapping("/register/save")
    public String saveCustomer(@ModelAttribute customers customer) {
        String hashedPassword = encryption.encrypt(customer.getCustomerPassword());
        customer.setCustomerPassword(hashedPassword);
        customer.setCustomerStatus("active");

        customerrepo.save(customer);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("customers", new customers());
        return "user/login";
    }

    @PostMapping("/login/success")
    public String loginSubmit(@RequestParam("cemail") String email,
            @RequestParam("CustomerPassword") String password, HttpSession session,
            Model model) {

        customers custo = customerrepo.findByCemail(email);
        if (custo == null) {
            return "redirect:/user/register";
        } else {
            if (encryption.matches(password, custo.getCustomerPassword())) {
                System.out.println("Login successful. Setting session attribute...");
                session.setAttribute("loginCustomer", custo.getCustomerId());
                System.out.println("Session customerId: " + session.getAttribute("loginCustomer"));
                return "redirect:/user/index";
            } else {
                model.addAttribute("loginError", "Invalid email or password");
                return "user/login";
            }
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login"; // Redirect to login page after logout
    }

    @GetMapping("forgot-password")
    public String forgotpassword() {
        return "user/forgot-password";
    }

    @GetMapping("404")
    public String error() {
        return "user/404";
    }

    @GetMapping("about")
    public String about() {
        return "user/about";
    }

    @GetMapping("blog-details")
    public String blogdetail() {
        return "user/blog-details";
    }

    @GetMapping("blog")
    public String blog() {
        return "user/blog";
    }

    @GetMapping("cart")
    public String cart(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        System.out.println("Retrieved loginCustomer ID from session: " + customerId);
        if (customerId == null) {
            redirectAttributes.addFlashAttribute("loginRequired", "Please log in to view your cart.");
            return "redirect:/user/login";
        }

        List<carts> cartItems = cartrepo.findByCustomerId(customerId);
        model.addAttribute("cartItems", cartItems);

        return "user/cart";
    }

    @PostMapping("cart/add")
    public String addToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        System.out.println("Received add to cart request for productId: " + productId + " with quantity: " + quantity);
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            redirectAttributes.addFlashAttribute("loginRequired", "Please log in to add items to your cart.");
            return "redirect:/user/login";
        }

        carts newCart = new carts();
        Integer nextId = cartrepo.findNextCartId();
        newCart.setId(nextId);
        newCart.setCustomerId(customerId);
        newCart.setProductId(productId);
        newCart.setQuantity(quantity);
        cartrepo.save(newCart);
        return "redirect:/user/cart"; // Redirect to the cart page
    }

    @GetMapping("checkout")
    public String checkout() {
        return "user/checkout";
    }

    @GetMapping("contact")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("faq")
    public String faq() {
        return "user/faq";
    }

    @GetMapping("my-account")
    public String myaccount() {
        return "user/my-account";
    }

    @GetMapping("product-details/{id}")
    public String productDetail(@PathVariable("id") int id, Model model) {
        products products = productrepo.findById(id).orElse(null);
        List<productotherimages> galleryImages = productimagerepo.findByProduct(products);
        List<String> galleryImageUrls = galleryImages.stream()
                .map(image -> "/productimages/" + image.getProductImage())
                .collect(Collectors.toList());
        List<products> relatedProducts = productrepo
                .findProductsByCategoryExcludingId(products.getProductCategory(), products.getProductId());
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("galleryImageUrls", galleryImageUrls);
        model.addAttribute("products", products);
        return "user/product-details";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(value = "category", required = false) String categoryName, Model model) {
        // Load all categories
        List<categories> categories = (List<categories>) caterepo.findAll();
        model.addAttribute("categories", categories);

        List<products> products;
        if (categoryName != null && !categoryName.isEmpty()) {
            products = productrepo.findProductsByCategory(categoryName);
        } else {
            products = (List<products>) productrepo.findAll();
        }

        products = products.stream()
                .filter(product -> !"block".equalsIgnoreCase(product.getProductStatus()))
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "user/shop";
    }

    @GetMapping("wishlist")
    public String wishlist() {
        return "user/wishlist";
    }
}