package com.example.demo.controller.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.blogs;
import com.example.demo.model.productimages;
import com.example.demo.model.products;
import com.example.demo.model.productsdto;
import com.example.demo.repository.adminrepository;
import com.example.demo.repository.blogrepository;
import com.example.demo.repository.customerrepository;
import com.example.demo.repository.orderrepository;
import com.example.demo.repository.productimagerepository;
import com.example.demo.repository.productrepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class productcontroller {

    @Autowired
    customerrepository customerrepo;

    @Autowired
    productrepository productrepo;

    @Autowired
    orderrepository orderrepo;

    @Autowired
    adminrepository adminrepo;

    @Autowired
    blogrepository blogrepo;

    @Autowired productimagerepository productimagerepo;

    // list product

    @GetMapping("apps-ecommerce-products")
    public String blog(Model model) {
        List<products> products = (List<products>) productrepo.findAll();
        model.addAttribute("products", products);
        return ("admin/apps-ecommerce-products");
    }

    // add product

    @GetMapping("apps-ecommerce-add-product")
    public String addProduct(Model model) {
        productsdto productsDto = new productsdto();
        model.addAttribute("productsdto", productsDto);
        return "admin/apps-ecommerce-add-product";
    }

    // Handle the submission of the product form
    @PostMapping("apps-ecommerce-add-product/save")
    public String saveProduct(@ModelAttribute("productsdto") productsdto productsDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/apps-ecommerce-add-product";
        }

        // Validate main image
        if (productsDto.getProductMainImage() == null || productsDto.getProductMainImage().isEmpty()) {
            result.addError(new FieldError("productsdto", "productMainImage", "Product main image is required"));
            return "admin/apps-ecommerce-add-product";
        }

        // Process main image
        MultipartFile mainImage = productsDto.getProductMainImage();
        String storageMainImageFilename = mainImage.getOriginalFilename();

        String uploadDirMain = "E:\\doanB\\projectB_cse411\\demo\\src\\main\\resources\\static\\productimages";
        Path uploadPathMain = Paths.get(uploadDirMain);

        try {
            // Create directory if it does not exist
            if (!Files.exists(uploadPathMain)) {
                Files.createDirectories(uploadPathMain);
            }

            // Save main image
            try (InputStream inputStream = mainImage.getInputStream()) {
                Path targetMainPath = uploadPathMain.resolve(storageMainImageFilename);
                if (!Files.exists(targetMainPath)) {
                    Files.copy(inputStream, targetMainPath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    System.out.println("Main image already exists: " + targetMainPath);
                }
            }

            // Declare an array to hold filenames for other images
            String[] otherImageFilenames = new String[4];

            // Handle other images if they exist
            if (productsDto.getProductOtherImages() != null && productsDto.getProductOtherImages().length > 0) {
                MultipartFile[] otherImages = productsDto.getProductOtherImages();

                for (int i = 0; i < otherImages.length && i < 4; i++) {
                    MultipartFile otherImage = otherImages[i];
                    String otherImageFilename = otherImage.getOriginalFilename();
                    otherImageFilenames[i] = otherImageFilename; // Store filename for DB

                    // Save other images
                    String uploadDirOther = "E:\\doanB\\projectB_cse411\\demo\\src\\main\\resources\\static\\otherimages";
                    Path uploadPathOther = Paths.get(uploadDirOther);

                    if (!Files.exists(uploadPathOther)) {
                        Files.createDirectories(uploadPathOther);
                    }

                    try (InputStream otherImageInputStream = otherImage.getInputStream()) {
                        Path targetOtherPath = uploadPathOther.resolve(otherImageFilename);
                        if (!Files.exists(targetOtherPath)) {
                            Files.copy(otherImageInputStream, targetOtherPath, StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            System.out.println("Other image already exists: " + targetOtherPath);
                        }
                    }
                }
            }

            // Create and save the product
            products pro = new products();
            pro.setProductId(productsDto.getProductId());
            pro.setProductName(productsDto.getProductName());
            pro.setProductPrice(productsDto.getProductPrice());
            pro.setProductDescription(productsDto.getProductDescription());
            pro.setProductCategory(productsDto.getProductCategory());
            pro.setProductQuantity(productsDto.getProductQuantity());
            pro.setProductMainImage(storageMainImageFilename);

            // Save the product to the repository
            productrepo.save(pro);

            // Save the product images
            productimages productImage = new productimages();
            productImage.setProductId(pro.getProductId()); // Set the foreign key
            productImage.setMainImage(storageMainImageFilename);
            productImage.setOtherImage1(otherImageFilenames.length > 0 ? otherImageFilenames[0] : null);
            productImage.setOtherImage2(otherImageFilenames.length > 1 ? otherImageFilenames[1] : null);
            productImage.setOtherImage3(otherImageFilenames.length > 2 ? otherImageFilenames[2] : null);
            productImage.setOtherImage4(otherImageFilenames.length > 3 ? otherImageFilenames[3] : null);

            // Save the product images to the repository
            productimagerepo.save(productImage);

        } catch (IOException e) {
            System.out.println("Error occurred while saving image: " + e.getMessage());
            result.addError(new FieldError("productsdto", "productMainImage", "Unable to save the image. Try again."));
            return "admin/apps-ecommerce-add-product";
        }

        // Redirect to the product listing page after saving
        return "redirect:/admin/apps-ecommerce-products";
    }

    // edit product

    @GetMapping("/apps-ecommerce-edit-product")
    public String editproduct(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("currentProductId");
        if (id == null) {
            model.addAttribute("errorMessage", "No product selected!");
            return "admin/apps-ecommerce-edit-product";
        }

        products product = productrepo.findById(id).orElse(null);
        if (product == null) {
            model.addAttribute("errorMessage", "Product not found!");
            return "admin/apps-ecommerce-edit-product";
        }

        model.addAttribute("product", product);
        return "admin/apps-ecommerce-edit-product";
    }

    @GetMapping("/set-current-product-id/{id}")
    public String setCurrentProductId(@PathVariable("id") int id, HttpSession session) {
        session.setAttribute("currentProductId", id);
        return "redirect:/admin/apps-ecommerce-edit-product";
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
