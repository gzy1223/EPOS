package com.example.epos.service.impl;

import com.example.epos.entity.Bill;
import com.example.epos.entity.Orders;
import com.example.epos.entity.Restaurant;
import com.example.epos.service.PDFService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFServiceImpl implements PDFService {
    @Override
    public PDDocument RegisterPDF(Restaurant restaurant) {
        String restaurantTitle = new String("Restaurant Registration");
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
            contentStream.newLineAtOffset(140, 750);
            contentStream.showText(restaurantTitle);
            contentStream.endText();
            //new line
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(60, 610);
            contentStream.showText("Restaurant Name: " + restaurant.getName());
            contentStream.newLine();
            contentStream.showText("Restaurant Address: " + restaurant.getAddress());
            contentStream.newLine();
            contentStream.showText("Restaurant Phone: " + restaurant.getPhone());
            contentStream.newLine();
            contentStream.showText("Restaurant Email: " + restaurant.getEmail());
            contentStream.newLine();
            contentStream.showText("Restaurant Description: " + restaurant.getDescription());
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return document;
    }

    @Override
    public PDDocument PasswordPDF(String password) {
        String restaurantTitle = new String("Your password");
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
            contentStream.newLineAtOffset(140, 750);
            contentStream.showText("Your password");
            contentStream.endText();
            //new line
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(60, 610);
            contentStream.showText(password);
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return document;
    }
    @Override
    public PDDocument OrderPDF(Bill Bill) {
        String restaurantTitle = new String("Your order");
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
            contentStream.newLineAtOffset(140, 750);
            contentStream.showText(Bill.getTitle());
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
            contentStream.newLineAtOffset(270, 690);
            contentStream.showText(Bill.getSubtitle());
            contentStream.endText();

            //Writing Multiple Lines
            //writing the customer details
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(60, 610);
            contentStream.showText("Restaurant Name: ");
            contentStream.newLine();
            contentStream.showText("Payment Details: ");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(170, 610);
            contentStream.showText(Bill.getRestaurantName());
            contentStream.newLine();
            contentStream.showText(Bill.getPaymentDetails());
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.newLineAtOffset(80, 540);
            contentStream.showText("Product Name");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.newLineAtOffset(200, 540);
            contentStream.showText("Unit Price");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.newLineAtOffset(310, 540);
            contentStream.showText("Quantity");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.newLineAtOffset(410, 540);
            contentStream.showText("Price");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(80, 520);
            int n = Bill.getProductNames().size();
            for(int i =0; i<n; i++) {
                contentStream.showText(Bill.getProductNames().get(i));
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(200, 520);
            for(int i =0; i<n; i++) {
                contentStream.showText("5");
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(310, 520);
            for(int i =0; i<n; i++) {
                contentStream.showText("1");
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(410, 520);
            for(int i =0; i<n; i++) {
               // price = ProductPrice.get(i)*ProductQty.get(i);
                contentStream.showText("5");
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.newLineAtOffset(310, (500-(20*n)));
            contentStream.showText("Total: ");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            //Calculating where total is to be written using number of products
            contentStream.newLineAtOffset(410, (500-(20*n)));
            contentStream.showText(String.valueOf(Bill.getTotalAmount()));
            contentStream.endText();

            contentStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return document;
    }

}
