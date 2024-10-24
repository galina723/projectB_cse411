package com.example.demo.controller.admin;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

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

import com.example.demo.model.products;
import com.example.demo.model.productsdto;
import com.example.demo.repository.adminrepository;
import com.example.demo.repository.blogrepository;
import com.example.demo.repository.customerrepository;
import com.example.demo.repository.orderrepository;
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

    // add product

    @GetMapping("apps-ecommerce-add-product")
    public String addproduct(Model model) {
        productsdto productsdto = new productsdto();
        model.addAttribute("productsdto", productsdto);
        return ("admin/apps-ecommerce-add-product");
    }

    @PostMapping("apps-ecommerce-add-product/save")
    public String saveproduct(@ModelAttribute("productsdto") productsdto productsdto, BindingResult result) {
        if (result.hasErrors()) {
            // Xử lý lỗi nếu có
        }

        if (productsdto.getProductMainImage().isEmpty()) {
            result.addError(new FieldError("productsdto", "ProductMainImage", "ProductMainImage is required"));
        }

        if (result.hasErrors()) {
            return "admin/apps-ecommerce-add-product";
        }

        MultipartFile image = productsdto.getProductMainImage();
        Date createtime = new Date();
        String storagefilename = createtime.getTime() + "_" + image.getOriginalFilename();

        // Sử dụng đường dẫn tuyệt đối
        String uploaddir = System.getProperty("user.dir") + "demo/src/main/resources/static/productimages";
        Path uploadpath = Paths.get(uploaddir).toAbsolutePath(); // Đảm bảo đường dẫn là tuyệt đối

        try {
            // Kiểm tra nếu thư mục không tồn tại, thì tạo thư mục mới
            if (!Files.exists(uploadpath)) {
                Files.createDirectories(uploadpath);
            }

            // Lưu ảnh vào thư mục đã chỉ định
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, uploadpath.resolve(storagefilename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while saving image: " + e.getMessage());
        }

        products pro = new products();
        pro.setProductId(productsdto.getProductId());
        pro.setProductName(productsdto.getProductName());
        pro.setProductPrice(productsdto.getProductPrice());
        pro.setProductDescription(productsdto.getProductDescription());
        pro.setProductCategory(productsdto.getProductCategory());
        pro.setProductQuantity(productsdto.getProductQuantity());
        pro.setProductMainImage(storagefilename); // Lưu tên file ảnh
        // pro.setCreateTime(createtime); // Uncomment nếu cần thiết
        productrepo.save(pro);

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
