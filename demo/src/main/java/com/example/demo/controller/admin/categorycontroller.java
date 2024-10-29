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

import com.example.demo.model.*;
import com.example.demo.repository.adminrepository;
import com.example.demo.repository.blogrepository;
import com.example.demo.repository.categoriesrepository;
import com.example.demo.repository.customerrepository;
import com.example.demo.repository.orderrepository;
import com.example.demo.repository.productrepository;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class categorycontroller {

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

    @Autowired
    categoriesrepository caterepo;

    //show list
    @GetMapping("apps-ecommerce-category")
    public String categories(Model model) {
        List<categories> categories = (List<categories>) caterepo.findAll();
        model.addAttribute("categories", categories);
        return "admin/apps-ecommerce-category";
    }

    // add category
    

    // delete
    @GetMapping("/categorydelete/{id}")
    public String categorydelete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            caterepo.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category not found or already deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the category.");
        }

        return "redirect:/admin/apps-ecommerce-category";
    }

}
