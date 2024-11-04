package com.example.demo.otherfunction;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.example.demo.model.customers;
import com.example.demo.model.products;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Service
public class ExportPDFService {

    public static void exportProductPDF(HttpServletResponse response, List<products> productList) throws IOException {
        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=products.pdf");

        // Create a new PDF document
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            try {
                // Set font for the document
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float margin = 50;
                float yPosition = page.getMediaBox().getHeight() - margin;
                float tableHeight = 20f;

                // Draw Table Header
                String[] headers = {"ID", "Name", "Category", "Description", "Price", "Quantity"};
                drawRow(contentStream, headers, yPosition, true);
                yPosition -= tableHeight;

                // Set font for rows
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                // Draw Table Rows
                for (products product : productList) {
                    String[] row = {
                        String.valueOf(product.getProductId()),
                        product.getProductName(),
                        product.getProductCategory(),
                        product.getProductDescription(),
                        String.valueOf(product.getProductPrice()),
                        String.valueOf(product.getProductQuantity())
                    };
                    drawRow(contentStream, row, yPosition, false);
                    yPosition -= tableHeight;

                    // Add a new page if there's no space left
                    if (yPosition < margin) {
                        contentStream.close(); // Close the current content stream
                        page = new PDPage(); // Create a new page
                        document.addPage(page); // Add the new page to the document
                        contentStream = new PDPageContentStream(document, page); // Create a new content stream for the new page
                        contentStream.setFont(PDType1Font.HELVETICA, 10); // Set the font for the new content stream
                        yPosition = page.getMediaBox().getHeight() - margin; // Reset the yPosition
                    }
                }
            } finally {
                // Close the content stream
                contentStream.close();
            }

            // Write the document to response
            document.save(response.getOutputStream());
        }
    }

    private static void drawRow(PDPageContentStream contentStream, String[] row, float yPosition, boolean isHeader) throws IOException {
        // Set font for header or body
        if (isHeader) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        } else {
            contentStream.setFont(PDType1Font.HELVETICA, 10);
        }

        float margin = 50;
        float tableWidth = 500;
        float columnWidth = tableWidth / row.length;

        for (int i = 0; i < row.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + i * columnWidth, yPosition);
            contentStream.showText(row[i]);
            contentStream.endText();
        }

        // Draw a line under the row
        contentStream.setLineWidth(1f);
        contentStream.moveTo(margin, yPosition - 2);
        contentStream.lineTo(margin + tableWidth, yPosition - 2);
        contentStream.stroke();
    }

    // Method to export customers to PDF
    public static void exportCustomerToPDF(HttpServletResponse response, List<customers> customerList) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=customers.pdf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            try {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float margin = 50;
                float yPosition = page.getMediaBox().getHeight() - margin;
                float tableHeight = 20f;

                // Draw Table Header
                String[] headers = {"ID", "Name", "Email", "Phone", "Address", "City", "Province"};
                draRow(contentStream, headers, yPosition, true);
                yPosition -= tableHeight;

                // Set font for rows
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                // Draw Table Rows
                for (customers customer : customerList) {
                    String[] row = {
                        String.valueOf(customer.getCustomerId()),
                        customer.getCustomerName(),
                        customer.getCemail(),
                        customer.getCustomerPhone(),
                        customer.getCustomerAddress(),
                        customer.getCustomerCity(),
                        customer.getCustomerProvince()
                    };
                    draRow(contentStream, row, yPosition, false);
                    yPosition -= tableHeight;

                    // Add a new page if there's no space left
                    if (yPosition < margin) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        yPosition = page.getMediaBox().getHeight() - margin;
                    }
                }
            } finally {
                contentStream.close();
            }

            document.save(response.getOutputStream());
        }
    }

    // Method to draw a row in the PDF
    private static void draRow(PDPageContentStream contentStream, String[] row, float yPosition, boolean isHeader) throws IOException {
        if (isHeader) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        } else {
            contentStream.setFont(PDType1Font.HELVETICA, 10);
        }

        float margin = 50;
        float tableWidth = 500; // Total width of the table
        float columnWidth = tableWidth / row.length; // Width of each column

        // Write each cell in the row
        for (int i = 0; i < row.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + i * columnWidth, yPosition);
            contentStream.showText(row[i]);
            contentStream.endText();
        }

        // Draw a line under the row
        contentStream.setLineWidth(1f);
        contentStream.moveTo(margin, yPosition - 2);
        contentStream.lineTo(margin + tableWidth, yPosition - 2);
        contentStream.stroke();
    }
    
}
