package com.example.epos.service.impl;

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
}
