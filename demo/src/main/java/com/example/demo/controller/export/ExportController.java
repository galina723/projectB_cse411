package com.example.demo.controller.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.customers;
import com.example.demo.model.products;
import com.example.demo.otherfunction.ExportCSVService;
import com.example.demo.otherfunction.ExportPDFService;
import com.example.demo.repository.customerrepository;
import com.example.demo.repository.productrepository;


import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class ExportController {

    @Autowired
    private ExportCSVService exportService;

    @Autowired
    private customerrepository customerrepo;

    @Autowired
    private productrepository productrepo;


    @GetMapping("/export/customers/csv")
    public void exportCSVcustomer(HttpServletResponse response) throws IOException {
        List<customers> customers = (List<com.example.demo.model.customers>) customerrepo.findAll();
        exportService.exportToCSVforcustomer(response, customers);
    }

    @GetMapping("/export/customers/pdf")
    public void exportcustomerToPDF(HttpServletResponse response) throws IOException {
        List<customers> custo = (List<com.example.demo.model.customers>) customerrepo.findAll();
        ExportPDFService.exportCustomerToPDF(response, custo);
    }

    @GetMapping("/export/products/csv")
    public void exportCSVproduct(HttpServletResponse response) throws IOException {
        List<products> pro = (List<com.example.demo.model.products>) productrepo.findAll();
        exportService.exportToCSVforproduct(response, pro);
    }

    @GetMapping("/export/products/pdf")
    public void exportproductToPDF(HttpServletResponse response) throws IOException {
        List<products> pro = (List<com.example.demo.model.products>) productrepo.findAll();
        ExportPDFService.exportProductPDF(response, pro);
    }

}
