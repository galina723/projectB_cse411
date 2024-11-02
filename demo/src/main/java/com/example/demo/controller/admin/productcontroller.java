package com.example.demo.controller.admin;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
        List<categories> categories = caterepo.findAllByIsActiveTrue();
        model.addAttribute("categories", categories);

        productsdto productsdto = new productsdto();
        int nextProductId = productrepo.findNextProductId();
        productsdto.setProductId(nextProductId);

        model.addAttribute("productsdto", productsdto);
        return ("admin/apps-ecommerce-add-product");
    }

    @PostMapping("apps-ecommerce-add-product")
    public String saveProduct(@ModelAttribute("productsdto") productsdto productsdto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/apps-ecommerce-add-product";
        }

        if (productsdto.getProductMainImage().isEmpty()) {
            result.addError(new FieldError("productsdto", "ProductMainImage", "ProductMainImage is required"));
            return "admin/apps-ecommerce-add-product";
        }

        MultipartFile image = productsdto.getProductMainImage();
        String storagefilename = image.getOriginalFilename();

        String uploaddir = "E:\\doanB\\projectB_cse411\\demo\\src\\main\\resources\\static\\productimages";

        Path uploadpath = Paths.get(uploaddir);

        try {
            if (!Files.exists(uploadpath)) {
                Files.createDirectories(uploadpath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Path targetPath = uploadpath.resolve(storagefilename);

                if (!Files.exists(targetPath)) {
                    Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    System.out.println("File already exists: " + targetPath.toString());
                }
            }

        } catch (IOException e) {
            System.out.println("Error occurred while saving image: " + e.getMessage());
            result.addError(new FieldError("productsdto", "ProductMainImage", "Unable to save the image. Try again."));
            return "admin/apps-ecommerce-add-product";
        }

        products pro = new products();
        pro.setProductId(productsdto.getProductId());
        pro.setProductName(productsdto.getProductName());
        pro.setProductPrice(productsdto.getProductPrice());
        pro.setProductDescription(productsdto.getProductDescription());
        pro.setProductCategory(productsdto.getProductCategory());
        pro.setProductQuantity(productsdto.getProductQuantity());
        pro.setProductMainImage(storagefilename);
        pro.setProductStatus(productsdto.getProductStatus());

        products savedProduct = productrepo.save(pro);
        // Process and save gallery images
        List<MultipartFile> galleryImages = productsdto.getProductOtherImages();
        System.out.println("Gallery Images Size: " + (galleryImages != null ? galleryImages.size() : "null"));
        if (galleryImages != null && !galleryImages.isEmpty()) {
            for (MultipartFile galleryImage : galleryImages) {
                if (galleryImage.isEmpty()) {
                    continue;
                }

                String galleryImageFilename = galleryImage.getOriginalFilename();
                Path galleryImagePath = uploadpath.resolve(galleryImageFilename);

                try (InputStream inputStream = galleryImage.getInputStream()) {
                    Files.copy(inputStream, galleryImagePath, StandardCopyOption.REPLACE_EXISTING);

                    productotherimages productImage = new productotherimages();
                    productImage.setProduct(savedProduct);
                    productImage.setProductImage(galleryImageFilename);
                    productimagerepo.save(productImage);
                } catch (IOException e) {
                    System.out.println("Error saving gallery image: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No gallery images provided.");
        }

        return "redirect:/admin/apps-ecommerce-products";
    }

    @GetMapping("/set-current-product-id/{id}")
    public String setCurrentProductId(@PathVariable("id") int id, HttpSession session) {
        session.setAttribute("currentProductId", id);
        return "redirect:/admin/apps-ecommerce-edit-product";
    }

    @GetMapping("/apps-ecommerce-edit-product")
    public String showEditForm(HttpSession session, Model model) {
        List<categories> categories = caterepo.findAllByIsActiveTrue();
        model.addAttribute("categories", categories);
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

        List<productotherimages> galleryImages = productimagerepo.findByProduct(product);
        List<String> galleryImageUrls = galleryImages.stream()
                .map(image -> "/productimages/" + image.getProductImage())
                .collect(Collectors.toList());
        model.addAttribute("galleryImageUrls", galleryImageUrls);

        return "admin/apps-ecommerce-edit-product";
    }

    @PostMapping("/apps-ecommerce-edit-product")
    public String saveEditedProduct(@ModelAttribute("productsdto") productsdto productsdto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/apps-ecommerce-edit-product";
        }

        products pro = productrepo.findById(productsdto.getProductId()).orElse(null);
        if (pro == null) {
            result.addError(new FieldError("productsdto", "productId", "Product not found!"));
            return "admin/apps-ecommerce-edit-product";
        }

        String uploadDir = "E:\\doanB\\projectB_cse411\\demo\\src\\main\\resources\\static\\productimages";
        Path uploadPath = Paths.get(uploadDir);

        MultipartFile image = productsdto.getProductMainImage();
        if (image != null && !image.isEmpty()) {
            String storageFilename = image.getOriginalFilename();
            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = image.getInputStream()) {
                    Path targetPath = uploadPath.resolve(storageFilename);
                    Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
                pro.setProductMainImage(storageFilename);
            } catch (IOException e) {
                result.addError(
                        new FieldError("productsdto", "ProductMainImage", "Unable to save the image. Try again."));
                return "admin/apps-ecommerce-edit-product";
            }
        } else {
            // If no new image is uploaded, keep the existing image
            pro.setProductMainImage(pro.getProductMainImage());
        }

        List<productotherimages> existingGalleryImages = productimagerepo.findByProduct(pro);
        List<MultipartFile> galleryImages = productsdto.getProductOtherImages(); // All uploaded images

        // Check if new images are uploaded
        boolean hasNewImages = galleryImages.stream()
                .anyMatch(galleryImage -> galleryImage != null && !galleryImage.isEmpty());

        // If new images are uploaded, delete existing ones
        if (hasNewImages) {
            // Delete old gallery images from the database
            for (productotherimages existingImage : existingGalleryImages) {
                productimagerepo.delete(existingImage);
            }

            // Save the new gallery images
            for (MultipartFile galleryImage : galleryImages) {
                if (galleryImage != null && !galleryImage.isEmpty()) {
                    String galleryImageFilename = galleryImage.getOriginalFilename();

                    try {
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }
                        try (InputStream inputStream = galleryImage.getInputStream()) {
                            Path targetPath = uploadPath.resolve(galleryImageFilename);
                            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }

                        productotherimages newImage = new productotherimages();
                        newImage.setProduct(pro);
                        newImage.setProductImage(galleryImageFilename);
                        productimagerepo.save(newImage);
                    } catch (IOException e) {
                        result.addError(new FieldError("productsdto", "productOtherImages",
                                "Unable to save the new gallery image. Try again."));
                        return "admin/apps-ecommerce-edit-product";
                    }
                }
            }
        }

        pro.setProductName(productsdto.getProductName());
        pro.setProductPrice(productsdto.getProductPrice());
        pro.setProductDescription(productsdto.getProductDescription());
        pro.setProductCategory(productsdto.getProductCategory());
        pro.setProductQuantity(productsdto.getProductQuantity());
        pro.setProductStatus(productsdto.getProductStatus());
        pro.setCreateTime(productsdto.getCreateTime());

        productrepo.save(pro);

        return "redirect:/admin/apps-ecommerce-products";
    }

    // delete product
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            products product = productrepo.findById(id).orElse(null);
            List<productotherimages> p = productimagerepo.findByProduct(product);
            for (productotherimages image : p) {
                productimagerepo.delete(image);
            }
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
