package com.example.demo.controller.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.blogs;
import com.example.demo.model.blogsdto;
import com.example.demo.model.categories;
import com.example.demo.model.products;
import com.example.demo.model.productsdto;
import com.example.demo.repository.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class blogcontroller {

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

    // líst blog

    @GetMapping("apps-ecommerce-blog")
    public String blog(Model model) {
        List<blogs> blogs = (List<blogs>) blogrepo.findAll();
        model.addAttribute("blogs", blogs);
        return ("admin/apps-ecommerce-blog");
    }

    // add blog

    @GetMapping("apps-ecommerce-create-blog")
    public String addproduct(Model model) {
        List<blogs> blogs = (List<blogs>) blogrepo.findAll();
        model.addAttribute("blogs", blogs);
        blogsdto blogsdto = new blogsdto();

        int nextBlogId = blogrepo.findNextBlogId();

        blogsdto.setBlogId(nextBlogId);
        model.addAttribute("blogsdto", blogsdto);
        return ("admin/apps-ecommerce-create-blog");
    }

    @PostMapping("apps-ecommerce-create-blog/save")
    public String saveProduct(@ModelAttribute("blogsdto") blogsdto blogsdto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/apps-ecommerce-create-blog";
        }

        if (blogsdto.getBlogImage().isEmpty()) {
            result.addError(new FieldError("blogsdto", "BlogImage", "BlogImage is required"));
            return "admin/apps-ecommerce-create-blog";
        }

        MultipartFile image = blogsdto.getBlogImage();
        String storagefilename = image.getOriginalFilename();

        String uploaddir = "E:\\doanB\\projectB_cse411\\demo\\src\\main\\resources\\static\\blogimages\\";

        System.out.println("Upload Directory: " + uploaddir);

        Path uploadpath = Paths.get(uploaddir);

        try {

            if (!Files.exists(uploadpath)) {
                Files.createDirectories(uploadpath);
                System.out.println("Directory created: " + uploadpath.toString());
            }

            try (InputStream inputStream = image.getInputStream()) {
                Path targetPath = uploadpath.resolve(storagefilename);

                System.out.println("Target File Path: " + targetPath.toString());
                if (!Files.exists(targetPath)) {
                    Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File successfully saved at: " + targetPath.toString());
                } else {
                    System.out.println("File already exists: " + targetPath.toString());
                }
            }

        } catch (IOException e) {

            System.out.println("Error occurred while saving image: " + e.getMessage());
            result.addError(new FieldError("blogsdto", "ProductMainImage", "Unable to save the image. Try again."));
            return "admin/apps-ecommerce-create-blog";
        }

        blogs bl = new blogs();
        bl.setBlogId(blogsdto.getBlogId());
        bl.setBlogTitle(blogsdto.getBlogTitle());
        bl.setBlogDescription(blogsdto.getBlogDescription());
        bl.setBlogStatus(blogsdto.getBlogStatus());
        bl.setBlogCreateDate(blogsdto.getBlogCreateDate());
        bl.setBlogPostBy(blogsdto.getBlogPostBy());
        bl.setBlogtag(blogsdto.getBlogtag());
        bl.setBlogImage(storagefilename);

        // Save the product to the repository
        blogrepo.save(bl);

        // Redirect to the product listing page after successful save
        return "redirect:/admin/apps-ecommerce-blog";
    }

    // edit product

    @GetMapping("/apps-ecommerce-edit-blog")
    public String editblog(HttpSession session, Model model) {
        Integer id = (Integer) session.getAttribute("currentProductId");
        if (id == null) {
            model.addAttribute("errorMessage", "No product selected!");
            return "admin/apps-ecommerce-edit-blog";
        }

        products product = productrepo.findById(id).orElse(null);
        if (product == null) {
            model.addAttribute("errorMessage", "Product not found!");
            return "admin/apps-ecommerce-edit-blog";
        }

        model.addAttribute("product", product);
        return "admin/apps-ecommerce-edit-blog";
    }

    @GetMapping("/set-current-blog-id/{id}")
    public String setCurrentBlogId(@PathVariable("id") int id, HttpSession session) {
        session.setAttribute("currentProductId", id);
        return "redirect:/admin/apps-ecommerce-edit-blog";
    }

    // delete product

    @GetMapping("/deleteblog/{id}")
    public String deleteblog(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            blogrepo.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Blog deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Blog not found or already deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the blog.");
        }

        return "redirect:/admin/apps-ecommerce-blogs";
    }

    @GetMapping("apps-ecommerce-blog-details")
    public String blogdetail() {
        return ("admin/apps-ecommerce-blog-details");
    }

}
