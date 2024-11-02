package com.example.demo.otherfunction;

import com.example.demo.model.customers;
import com.example.demo.model.products;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class ExportCSVService {

    public void exportToCSVforcustomer(HttpServletResponse response, List<customers> customers) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"customers.csv\"");

        PrintWriter writer = response.getWriter();

        // Write CSV header
        writer.println("Customer ID,Name,Email,Phone,Address,City,Province");

        // Write data rows
        for (customers customer : customers) {
            writer.printf("%d,%s,%s,%s,%s,%s,%s%n",
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    customer.getCemail(), // Add missing email here
                    customer.getCustomerPhone(),
                    customer.getCustomerAddress(),
                    customer.getCustomerCity(),
                    customer.getCustomerProvince());
        }

        writer.flush();
        writer.close();
    }

    public void exportToCSVforproduct(HttpServletResponse response, List<products> product) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"products.csv\"");

        PrintWriter writer = response.getWriter();

        // Write CSV header
        writer.println("Product ID,Name,Category,Description,Price,Quantity");

        // Write data rows
        for (products pro : product) {
            writer.printf("%d,%s,%s,%s,%s,%s%n",
                    pro.getProductId(),
                    pro.getProductName(),
                    pro.getProductCategory(),
                    pro.getProductDescription(),
                    pro.getProductPrice(),
                    pro.getProductQuantity()
                    );
        }

        writer.flush();
        writer.close();
    }

}
