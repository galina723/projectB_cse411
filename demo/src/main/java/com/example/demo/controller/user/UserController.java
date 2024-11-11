package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import jakarta.servlet.http.HttpSession;

import com.example.demo.otherfunction.encryption;

import java.util.*;
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

    @Autowired
    private blogrepository blogrepo;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId != null) {
            List<carts> cartItems = cartrepo.findByCustomerId(customerId);
            List<cartsdto> cartItemDTOs = new ArrayList<>();
            double subtotal = 0.0;

            for (carts cartItem : cartItems) {
                products product = cartItem.getProduct();
                if (product != null) {
                    double totalPrice = product.getProductPrice() * cartItem.getQuantity();
                    subtotal += totalPrice;
                    cartsdto dto = new cartsdto();
                    dto.setProductId(product.getProductId());
                    dto.setProductName(product.getProductName());
                    dto.setProductImage(product.getProductMainImage());
                    dto.setProductPrice(product.getProductPrice());
                    dto.setQuantity(cartItem.getQuantity());
                    dto.setTotalPrice(totalPrice);
                    cartItemDTOs.add(dto);
                }
            }

            double shippingFee = subtotal * 0.01;
            double total = subtotal + shippingFee;

            model.addAttribute("cartItems", cartItemDTOs);
            model.addAttribute("cartItemCount", cartItemDTOs.size());
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("total", total);
        } else {
            model.addAttribute("cartItemCount", 0);
        }
    }

    @GetMapping("index")
    public String index(Model model) {
        List<categories> categories = (List<categories>) caterepo.findAll();
        model.addAttribute("categories", categories);

        Pageable pageable = PageRequest.of(0, 8);
        List<products> products = productrepo.findTop10Products(pageable);
        model.addAttribute("products", products);
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
                session.setAttribute("loginCustomer", custo.getCustomerId());

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

    @GetMapping("blog-details/{id}")
    public String blogdetail(@PathVariable("id") int id, Model model) {
        blogs blogs = blogrepo.findById(id).orElse(null);
        List<blogs> recentBlogs = blogrepo.findTop3ByIdNot(id);
        model.addAttribute("recentBlogs", recentBlogs);
        model.addAttribute("blogs", blogs);
        return "user/blog-details";
    }

    @GetMapping("blog")
    public String blog(Model model) {
        List<blogs> blogs = (List<blogs>) blogrepo.findAll();
        blogs = blogs.stream()
                .filter(blog -> !"hidden".equalsIgnoreCase(blog.getBlogStatus()))
                .collect(Collectors.toList());
        model.addAttribute("blogs", blogs);
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

        // Calculate total for each cart item
        double subtotal = 0.0;
        List<cartsdto> cartItemDTOs = new ArrayList<>();
        for (carts cartItem : cartItems) {
            products product = cartItem.getProduct(); // Fetch the product details

            if (product != null) { // Ensure product is not null
                double totalPrice = product.getProductPrice() * cartItem.getQuantity(); // Calculate total price
                subtotal += totalPrice;
                cartsdto dto = new cartsdto();
                dto.setProductId(product.getProductId());
                dto.setProductName(product.getProductName()); // Assuming 'getName()' returns the product name
                dto.setProductImage(product.getProductMainImage()); // Assuming 'getImageUrl()' returns the product
                                                                    // image URL
                dto.setProductPrice(product.getProductPrice());
                dto.setQuantity(cartItem.getQuantity());
                dto.setTotalPrice(totalPrice);

                cartItemDTOs.add(dto);
            }
        }
        double shippingFee = 0.0;
        if (subtotal < 500) {
            shippingFee = subtotal * 0.01;
        }
        double total = subtotal + shippingFee;

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("total", total);

        model.addAttribute("cartItems", cartItemDTOs);
        model.addAttribute("cartItemCount", cartItemDTOs.size());
        return "user/cart";
    }

    @GetMapping("/cart/delete/{productId}")
    public String deleteCartItem(@PathVariable Integer productId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Integer customerId = (Integer) session.getAttribute("loginCustomer");

        if (customerId == null) {
            redirectAttributes.addFlashAttribute("loginRequired", "Please log in to modify your cart.");
            return "redirect:/user/login";
        }

        CartId cartId = new CartId(customerId, productId);

        try {
            cartrepo.deleteById(cartId);
            redirectAttributes.addFlashAttribute("successMessage", "Item removed from cart successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to remove item from cart.");
        }
        return "redirect:/user/cart"; // Redirect back to the cart
    }

    // @PostMapping("cart/add")
    // public String addToCart(@RequestParam("productId") int productId,
    // @RequestParam("quantity") int quantity,
    // HttpSession session,
    // RedirectAttributes redirectAttributes) {
    // Integer customerId = (Integer) session.getAttribute("loginCustomer");
    // if (customerId == null) {
    // redirectAttributes.addFlashAttribute("loginRequired", "Please log in to add
    // items to your cart.");
    // return "redirect:/user/login";
    // }

    // customers customer = customerrepo.findById(customerId).orElse(null);
    // if (customer == null) {
    // redirectAttributes.addFlashAttribute("error", "Customer not found.");
    // return "redirect:/user/cart";
    // }

    // // Ensure the product exists
    // products product = productrepo.findById(productId).orElse(null);
    // if (product == null) {
    // redirectAttributes.addFlashAttribute("error", "Product not found.");
    // return "redirect:/user/cart";
    // }

    // carts newCart = new carts();
    // CartId cartId = new CartId();
    // cartId.setCustomerId(customerId);
    // cartId.setProductId(productId);
    // newCart.setId(cartId);

    // // Set the customer and product properly
    // newCart.setCustomer(customer); // Set the actual customer object
    // newCart.setProduct(product); // Set the actual product object
    // newCart.setQuantity(quantity);
    // cartrepo.save(newCart);

    // return "redirect:/user/cart";
    // }

    @PostMapping("cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {
        // Check if the user is logged in
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Please log in to add items to your cart."));
        }

        // Fetch the customer
        customers customer = customerrepo.findById(customerId).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Customer not found."));
        }

        // Fetch the product
        products product = productrepo.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Product not found."));
        }

        // Create and save the new cart entry
        carts newCart = new carts();
        CartId cartId = new CartId();
        cartId.setCustomerId(customerId);
        cartId.setProductId(productId);
        newCart.setId(cartId);
        newCart.setCustomer(customer);
        newCart.setProduct(product);
        newCart.setQuantity(quantity);
        cartrepo.save(newCart);

        // Prepare response data
        List<carts> cartItems = cartrepo.findByCustomerId(customerId);
        List<cartsdto> cartItemDTOs = new ArrayList<>();
        double subtotal = 0.0;

        for (carts cartItem : cartItems) {
            products cartProduct = cartItem.getProduct();
            if (cartProduct != null) {
                double totalPrice = cartProduct.getProductPrice() * cartItem.getQuantity();
                subtotal += totalPrice;

                cartsdto dto = new cartsdto();
                dto.setProductId(cartProduct.getProductId());
                dto.setProductName(cartProduct.getProductName());
                dto.setProductImage(cartProduct.getProductMainImage());
                dto.setProductPrice(cartProduct.getProductPrice());
                dto.setQuantity(cartItem.getQuantity());
                dto.setTotalPrice(totalPrice);
                cartItemDTOs.add(dto);
            }
        }

        double shippingFee = subtotal < 500 ? subtotal * 0.01 : 0.0;
        double total = subtotal + shippingFee;

        Map<String, Object> response = new HashMap<>();
        response.put("cartItemCount", cartItemDTOs.size());
        response.put("cartItems", cartItemDTOs);
        response.put("subtotal", subtotal);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Map<String, String> params, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            redirectAttributes.addFlashAttribute("loginRequired", "Please log in to update your cart.");
            return "redirect:/user/login";
        }

        // Get the cart items for the current customer
        List<carts> cartItems = cartrepo.findByCustomerId(customerId);

        // Iterate over each cart item and update the quantity
        for (carts cartItem : cartItems) {
            String quantityParam = params.get("quantity-" + cartItem.getProduct().getProductId());
            if (quantityParam != null) {
                int quantity = Integer.parseInt(quantityParam);
                if (quantity > 0) {
                    cartItem.setQuantity(quantity); // Update the quantity
                    cartrepo.save(cartItem); // Save the updated cart item
                }
            }
        }

        return "redirect:/user/cart"; // Redirect back to the cart page
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