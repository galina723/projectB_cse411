package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.SendEmailService;

import jakarta.servlet.http.HttpSession;

import com.example.demo.otherfunction.JsonLoader;
import com.example.demo.otherfunction.encryption;

import java.io.IOException;
import java.net.URI;
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

    @Autowired
    private orderrepository orderrepo;

    @Autowired
    private JsonLoader jsonLoader;

    @Autowired
    private SendEmailService sendEmailService;

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

        int defaultCategoryId = categories.isEmpty() ? 0 : categories.get(0).getCategoryId();
        model.addAttribute("selectedCategoryId", defaultCategoryId);

        List<products> products2 = productrepo.findProductsByCategoryId(defaultCategoryId, pageable);
        products2 = products2.stream()
                .filter(product2 -> !"block".equalsIgnoreCase(product2.getProductStatus()))
                .collect(Collectors.toList());
        model.addAttribute("productsCategory", products2);

        products = products.stream()
                .filter(product -> !"block".equalsIgnoreCase(product.getProductStatus()))
                .collect(Collectors.toList());
        model.addAttribute("products", products);

        List<blogs> blogs = (List<blogs>) blogrepo.findAll();
        blogs = blogs.stream()
                .filter(blog -> !"hidden".equalsIgnoreCase(blog.getBlogStatus()))
                .collect(Collectors.toList());
        model.addAttribute("blogs", blogs);
        return "user/index";
    }

    @GetMapping("/index/{categoryId}")
    public String getProductsByCategory(@PathVariable("categoryId") int categoryId, Model model) {
        Pageable pageable = PageRequest.of(0, 8);
        List<products> products = productrepo.findTop10Products(pageable);

        products = products.stream()
                .filter(product -> !"block".equalsIgnoreCase(product.getProductStatus()))
                .collect(Collectors.toList());
        model.addAttribute("products", products);

        List<blogs> blogs = (List<blogs>) blogrepo.findAll();
        blogs = blogs.stream()
                .filter(blog -> !"hidden".equalsIgnoreCase(blog.getBlogStatus()))
                .collect(Collectors.toList());
        model.addAttribute("blogs", blogs);

        List<categories> categories = (List<categories>) caterepo.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("selectedCategoryId", categoryId);

        String categoryName = caterepo.findById(categoryId).orElse(null).getCategoryName();
        System.out.println("Category Name: " + categoryName);
        model.addAttribute("categoryName", categoryName);

        List<products> products2 = productrepo.findProductsByCategoryId(categoryId, pageable);

        products = products.stream()
                .filter(product -> !"block".equalsIgnoreCase(product.getProductStatus()))
                .collect(Collectors.toList());
        model.addAttribute("productsCategory", products2);

        return "user/index";
    }

    @GetMapping("register")
    public String register(Model model) {
        customers customer = new customers();
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
        return "user/login";
    }

    @PostMapping("/login/success")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginSubmit(@RequestParam("cemail") String email,
            @RequestParam("CustomerPassword") String password, HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        customers custo = customerrepo.findByCemail(email);

        if (custo == null || !encryption.matches(password, custo.getCustomerPassword())) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        session.setAttribute("loginCustomer", custo.getCustomerId());
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "user/forgot-password";
    }

    @PostMapping("/forgot-password/send-code")
    public String sendResetCode(@RequestParam("email") String cemail, HttpSession session, Model model) {
        customers customer = customerrepo.findByCemail(cemail);

        if (customer != null) {
            String resetCode = sendEmailService.generateResetCode();

            session.setAttribute("resetCode", resetCode);
            session.setAttribute("customerId", customer.getCustomerId());

            String body = "Your password reset code is: " + resetCode;
            sendEmailService.sendEmail(cemail, body, "Password Reset Code");

            return "redirect:/user/forgot-password/verify";
        } else {
            model.addAttribute("error", "Email not found.");
            return "user/forgot-password";
        }
    }

    @GetMapping("/forgot-password/verify")
    public String verifyCodePage() {
        return "user/verify-code";
    }

    @PostMapping("/forgot-password/verify-code")
    public String verifyResetCode(@RequestParam("code") String code, HttpSession session, Model model) {
        String sessionCode = (String) session.getAttribute("resetCode");

        if (sessionCode != null && sessionCode.equals(code)) {
            return "user/reset-password";
        } else {
            model.addAttribute("error", "Invalid reset code.");
            return "user/verify-code";
        }
    }

    @GetMapping("/forgot-password/reset")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model) {

        model.addAttribute("cemail", email);
        return "user/reset-password";
    }

    @PostMapping("/forgot-password/reset")
    public String resetPassword(
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("email") String email,
            HttpSession session,
            Model model) {
        int customerId = (int) session.getAttribute("customerId");
        customers customer = customerrepo.findById(customerId).orElse(null);

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu không khớp, vui lòng thử lại.");
            return "user/reset-password";
        }

        String encodedPassword = encryption.encrypt(newPassword);
        customer.setCustomerPassword(encodedPassword);

        customerrepo.save(customer);

        return "redirect:/user/login";
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

        double subtotal = 0.0;
        List<cartsdto> cartItemDTOs = new ArrayList<>();
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
        return "redirect:/user/cart";
    }

    @PostMapping("cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/user/login"))
                    .build();
        }

        customers customer = customerrepo.findById(customerId).orElse(null);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Customer not found."));
        }

        products product = productrepo.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Product not found."));
        }

        if (quantity > product.getProductQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message",
                            "Requested quantity exceeds available stock. Available: " + product.getProductQuantity()));
        }

        carts existingCartItem = cartrepo.findByCustomerIdAndProductId(customerId, productId);
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;
            if (newQuantity > product.getProductQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Total quantity in cart exceeds available stock. Available: "
                                + product.getProductQuantity()));
            }
            existingCartItem.setQuantity(newQuantity);
            cartrepo.save(existingCartItem);
        } else {
            carts newCart = new carts();
            CartId cartId = new CartId();
            cartId.setCustomerId(customerId);
            cartId.setProductId(productId);
            newCart.setId(cartId);
            newCart.setCustomer(customer);
            newCart.setProduct(product);
            newCart.setQuantity(quantity);
            cartrepo.save(newCart);
        }

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

        List<carts> cartItems = cartrepo.findByCustomerId(customerId);
        boolean quantityExceeded = false;

        for (carts cartItem : cartItems) {
            String quantityParam = params.get("quantity-" + cartItem.getProduct().getProductId());
            if (quantityParam != null) {
                int requestedQuantity = Integer.parseInt(quantityParam);
                int availableQuantity = cartItem.getProduct().getProductQuantity();

                if (requestedQuantity > availableQuantity) {
                    quantityExceeded = true;
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Quantity for product " + cartItem.getProduct().getProductName() +
                                    " exceeds available stock (" + availableQuantity + " available).");
                } else if (requestedQuantity > 0) {
                    cartItem.setQuantity(requestedQuantity);
                    cartrepo.save(cartItem);
                }
            }
        }

        if (quantityExceeded) {
            return "redirect:/user/cart";
        }

        return "redirect:/user/cart";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            return "redirect:/user/login";
        }

        customers customer = customerrepo.findById(customerId).orElse(null);
        if (customer == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("customers", customer);

        List<carts> cartItems = cartrepo.findByCustomerId(customerId);
        if (cartItems.isEmpty()) {
            model.addAttribute("emptyCart", true);
            model.addAttribute("checkoutSuccess", false);
            return "user/checkout";
        }

        double subtotal = 0.0;
        List<cartsdto> cartItemDTOs = new ArrayList<>();
        for (carts cartItem : cartItems) {
            products product = cartItem.getProduct();
            if (product != null) {
                double totalPrice = product.getProductPrice() * cartItem.getQuantity();
                subtotal += totalPrice;
                cartsdto dto = new cartsdto();
                dto.setProductName(product.getProductName());
                dto.setQuantity(cartItem.getQuantity());
                dto.setTotalPrice(totalPrice);
                cartItemDTOs.add(dto);
            }
        }

        double shippingFee = (subtotal < 500) ? subtotal * 0.01 : 0.0;
        double total = subtotal + shippingFee;

        List<provinces> provincesList;
        try {
            provincesList = jsonLoader.loadProvinces();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("total", total);
        model.addAttribute("cartItems", cartItemDTOs);
        model.addAttribute("emptyCart", false);
        model.addAttribute("checkoutSuccess", false);
        model.addAttribute("provinces", provincesList);

        return "user/checkout";
    }

    @GetMapping("/districts/{provinceId}")
    @ResponseBody
    public List<districts> getDistricts(@PathVariable String provinceId) {
        try {
            return jsonLoader.loadDistricts(provinceId);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @PostMapping("/checkout/confirmation")
    public String processCheckout(
            HttpSession session, Model model,
            @RequestParam(value = "orderAddress") String address,
            @RequestParam(value = "orderCity") String city,
            @RequestParam(value = "orderProvince") String province,
            @RequestParam(value = "orderNote", required = false) String note) {

        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            return "redirect:/user/login";
        }

        List<carts> cartItems = cartrepo.findByCustomerId(customerId);
        if (cartItems.isEmpty()) {
            session.setAttribute("emptyCart", true);
            return "redirect:/user/checkout";
        }

        double subtotal = 0.0;
        for (carts cartItem : cartItems) {
            products product = cartItem.getProduct();
            if (product != null) {
                subtotal += product.getProductPrice() * cartItem.getQuantity();
            }
        }

        double shippingFee = (subtotal < 500) ? subtotal * 0.01 : 0.0;
        double total = subtotal + shippingFee;

        String provinceName = jsonLoader.getProvinceNameById(province);

        orders order = new orders();
        order.setCustomerId(String.valueOf(customerId));
        order.setOrderDate(new java.sql.Date(new Date().getTime()));
        order.setOrderStatus("Pending");
        order.setOrderAmount((int) total);
        order.setOrderPaymentMethod("COD");
        order.setOrderNote(note != null ? note : "");
        order.setOrderAddress(address);
        order.setOrderCity(city);
        order.setOrderProvince(provinceName);
        orderrepo.save(order);

        for (carts cartItem : cartItems) {
            products product = cartItem.getProduct();
            if (product != null) {
                int newQuantity = product.getProductQuantity() - cartItem.getQuantity();
                if (newQuantity < 0)
                    newQuantity = 0;
                product.setProductQuantity(newQuantity);
                productrepo.save(product);
            }
        }

        cartrepo.deleteAllByCustomerId(customerId);

        session.setAttribute("checkoutSuccess", true);
        return "redirect:/user/index";
    }

    @GetMapping("/checkout/clearCheckoutSuccess")
    public String index(HttpSession session, Model model) {
        Boolean checkoutSuccess = (Boolean) session.getAttribute("checkoutSuccess");

        if (checkoutSuccess != null && checkoutSuccess) {
            model.addAttribute("checkoutSuccess", true);
            session.removeAttribute("checkoutSuccess");
        }

        return "user/index";
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
    public String myaccount(HttpSession session, Model model) {
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            return "redirect:/user/login";
        }

        List<orders> orders = orderrepo.findOrdersByCustomerId(customerId);
        model.addAttribute("orders", orders);

        customers customer = customerrepo.findById(customerId).orElse(null);
        model.addAttribute("customer", customer);

        List<provinces> provincesList;
        try {
            provincesList = jsonLoader.loadProvinces();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        model.addAttribute("provinces", provincesList);
        return "user/my-account";
    }

    @PostMapping("/my-account")
    public String updateAccount(@ModelAttribute("customer") customers updatedCustomer, HttpSession session) {
        Integer customerId = (Integer) session.getAttribute("loginCustomer");
        if (customerId == null) {
            return "redirect:/user/login";
        }

        customers customer = customerrepo.findById(customerId).orElse(null);
        if (customer != null) {
            customer.setCustomerName(updatedCustomer.getCustomerName());
            customer.setCustomerAddress(updatedCustomer.getCustomerAddress());
            customer.setCustomerCity(updatedCustomer.getCustomerCity());
            customer.setCustomerProvince(jsonLoader.getProvinceNameById(updatedCustomer.getCustomerProvince()));
            customerrepo.save(customer);
        }

        return "redirect:/user/my-account";
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
}