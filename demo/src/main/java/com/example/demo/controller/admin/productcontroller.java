package com.example.demo.controller.admin;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import jakarta.servlet.http.*;

@Controller
@RequestMapping("/admin")
public class productcontroller {

    @Autowired
    categoriesrepository caterepo;

    @Autowired
    productrepository productrepo;

    @Autowired
    productimagesrepository productimagerepo;

    // add product

    @GetMapping("apps-ecommerce-products")
    public String products(Model model) {
        List<products> productList = (List<products>) productrepo.findAll();
        model.addAttribute("products", productList);
        return "admin/apps-ecommerce-products";
    }

    @GetMapping("apps-ecommerce-add-product")
    public String addproduct(Model model) {
        List<categories> categories = (List<categories>) caterepo.findAll(); // Fetch categories from the database
        model.addAttribute("categories", categories); // Add categories to the model
        productsdto productsdto = new productsdto();
        // Get the next available ProductId
        int nextProductId = productrepo.findNextProductId();
        // Set the ProductId in the DTO
        productsdto.setProductId(nextProductId);
        model.addAttribute("productsdto", productsdto);
        return ("admin/apps-ecommerce-add-product");
    }

    @PostMapping("apps-ecommerce-add-product")
    public String saveProduct(@ModelAttribute("productsdto") productsdto productsdto, BindingResult result) {
        // Check for form validation errors
        if (result.hasErrors()) {
            return "admin/apps-ecommerce-add-product";
        }

        // Check if the uploaded image is empty
        if (productsdto.getProductMainImage().isEmpty()) {
            result.addError(new FieldError("productsdto", "ProductMainImage", "ProductMainImage is required"));
            return "admin/apps-ecommerce-add-product"; // Return to the form if there's an error
        }

        MultipartFile image = productsdto.getProductMainImage();
        String storagefilename = image.getOriginalFilename(); // Get the original file name

        // Define the absolute path for the images directory
        String uploaddir = "C:\\Users\\Admin\\Downloads\\Cosmetic\\projectB_cse311\\demo\\src\\main\\resources\\static\\productimages";
        // Print the upload directory for debugging
        System.out.println("Upload Directory: " + uploaddir);

        Path uploadpath = Paths.get(uploaddir);

        try {
            // Ensure the directory exists, if not, create it
            if (!Files.exists(uploadpath)) {
                Files.createDirectories(uploadpath);
                System.out.println("Directory created: " + uploadpath.toString());
            }

            // Save the uploaded image to the directory
            try (InputStream inputStream = image.getInputStream()) {
                Path targetPath = uploadpath.resolve(storagefilename);

                // Print target path for debugging
                System.out.println("Target File Path: " + targetPath.toString());

                // Only copy the file if it does not already exist
                if (!Files.exists(targetPath)) {
                    Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File successfully saved at: " + targetPath.toString());
                } else {
                    System.out.println("File already exists: " + targetPath.toString());
                }
            }

        } catch (IOException e) {
            // Handle file saving error
            System.out.println("Error occurred while saving image: " + e.getMessage());
            result.addError(new FieldError("productsdto", "ProductMainImage", "Unable to save the image. Try again."));
            return "admin/apps-ecommerce-add-product";
        }

        // Create a new product entity and set its fields
        products pro = new products();
        pro.setProductId(productsdto.getProductId());
        pro.setProductName(productsdto.getProductName());
        pro.setProductPrice(productsdto.getProductPrice());
        pro.setProductDescription(productsdto.getProductDescription());
        pro.setProductCategory(productsdto.getProductCategory());
        pro.setProductQuantity(productsdto.getProductQuantity());
        pro.setProductMainImage(storagefilename);
        pro.setProductStatus(productsdto.getProductStatus());

        // Save the product to the repository
        productrepo.save(pro);

        // Redirect to the product listing page after successful save
        return "redirect:/admin/apps-ecommerce-products";
    }

    @GetMapping("/set-current-product-id/{id}")
    public String setCurrentProductId(@PathVariable("id") int id, HttpSession session) {
        session.setAttribute("currentProductId", id); // Store as Integer
        return "redirect:/admin/apps-ecommerce-edit-product";
    }

    @GetMapping("/apps-ecommerce-edit-product")
    public String showEditForm(HttpSession session, Model model) {
        List<categories> categories = (List<categories>) caterepo.findAll(); // Fetch categories from the database
        model.addAttribute("categories", categories); // Add categories to the model
        Integer productId = (Integer) session.getAttribute("currentProductId");
        if (productId == null) {
            model.addAttribute("errorMessage", "No product selected!");
            return "admin/apps-ecommerce-edit-product";
        }

        products product = productrepo.findById(productId).orElse(null);
        if (product == null) {
            model.addAttribute("errorMessage", "Product not found!");
            return "admin/apps-ecommerce-edit-product";
        }

        productsdto productsDto = new productsdto();
        productsDto.setProductId(product.getProductId());
        productsDto.setProductName(product.getProductName());
        productsDto.setProductPrice(product.getProductPrice());
        productsDto.setProductDescription(product.getProductDescription());
        productsDto.setProductCategory(product.getProductCategory());
        productsDto.setProductQuantity(product.getProductQuantity());
        productsDto.setProductStatus(product.getProductStatus());
        productsDto.setCreateTime(product.getCreateTime());

        model.addAttribute("productsdto", productsDto);
        // Pass the existing image URL to the model for displaying in the view
        model.addAttribute("existingImage", "/productimages/" + product.getProductMainImage());

        return "admin/apps-ecommerce-edit-product";
    }

    @PostMapping("/apps-ecommerce-edit-product")

    public String saveEditedProduct(@ModelAttribute("productsdto") productsdto productsdto, BindingResult result) {

        if (result.hasErrors()) {
            return "admin/apps-ecommerce-edit-product"; // Return to form if there are errors
        }

        products pro = productrepo.findById(productsdto.getProductId()).orElse(null);
        if (pro == null) {
            result.addError(new FieldError("productsdto", "productId", "Product not found!"));
            return "admin/apps-ecommerce-edit-product"; // Return to the form if the product is not found
        }

        // Check if a new image is uploaded
        MultipartFile image = productsdto.getProductMainImage();
        if (image != null && !image.isEmpty()) {
            String uploadDir = "C:\\Users\\Admin\\Downloads\\Cosmetic\\projectB_cse311\\demo\\src\\main\\resources\\static\\productimages";
            Path uploadPath = Paths.get(uploadDir);
            String storageFilename = image.getOriginalFilename();

            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath); // Create directory if it doesn't exist
                }
                try (InputStream inputStream = image.getInputStream()) {
                    Path targetPath = uploadPath.resolve(storageFilename);
                    Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
                pro.setProductMainImage(storageFilename); // Save only the filename in the entity
            } catch (IOException e) {
                result.addError(
                        new FieldError("productsdto", "ProductMainImage", "Unable to save the image. Try again."));
                return "admin/apps-ecommerce-edit-product"; // Return to the form on error
            }
        } else {
            // If no new image is uploaded, keep the existing image
            pro.setProductMainImage(pro.getProductMainImage());
        }

        // Set other fields
        pro.setProductName(productsdto.getProductName());
        pro.setProductPrice(productsdto.getProductPrice());
        pro.setProductDescription(productsdto.getProductDescription());
        pro.setProductCategory(productsdto.getProductCategory());
        pro.setProductQuantity(productsdto.getProductQuantity());
        pro.setProductStatus(productsdto.getProductStatus());
        pro.setCreateTime(productsdto.getCreateTime());

        productrepo.save(pro); // Save the updated product

        return "redirect:/admin/apps-ecommerce-products"; // Redirect to the product listing page
    }

    // delete product

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            productrepo.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found or already deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the product.");
        }

        return "redirect:/admin/apps-ecommerce-products";
    }
}
