package com.example.epos.service.impl;

import com.example.epos.dto.MenuforBillDto;
import com.example.epos.entity.Bill;
import com.example.epos.entity.Orders;
import com.example.epos.entity.Restaurant;
import com.example.epos.firemapper.OrderMapper;
import com.example.epos.service.PDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.itextpdf.text.pdf.PdfName.DEST;

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
    public void PrintPDF(String filePath) {

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
            ArrayList<String> Quantity = new ArrayList<>();
            ArrayList<String> price = new ArrayList<>();
            for(int i =0; i<n; i++) {
                // get the string of the document
                String orderName = Bill.getProductNames().get(i);
                if (orderName.equals("garbageValue")){
                    continue;
                }
                //get the volume of the order
                int colonIndex = orderName.indexOf(":");
                if (colonIndex != -1){
                    String afterColon = orderName.substring(colonIndex + 1);
                    int volume = Integer.parseInt(afterColon);
                    Quantity.add(String.valueOf(volume));
                }
                //get the name of the product before the ":"
                String beforeColon = orderName.substring(0, colonIndex);
                //get the menus in the seller document
                MenuforBillDto menu = OrderMapper.getMenus(beforeColon, Bill.getSellerUID());
                price.add(menu.getPrice());
                contentStream.showText(menu.getName());
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(200, 520);
            for(int i =0; i<n-1; i++) {
                contentStream.showText(price.get(i));
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(310, 520);
            for(int i =0; i<n-1; i++) {
                contentStream.showText(Quantity.get(i).toString());
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(410, 520);
            BigDecimal total = new BigDecimal(0);
            for(int i =0; i<n-1; i++) {
               //accurately calcualte the price
                total = total.add(new BigDecimal(price.get(i)).multiply(new BigDecimal(Quantity.get(i))));
                contentStream.showText(total.toString());
                contentStream.newLine();
            }
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            contentStream.newLineAtOffset(310, (500-(20*(n-1))));
            contentStream.showText("Total: ");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
            //Calculating where total is to be written using number of products
            contentStream.newLineAtOffset(410, (500-(20*(n-1))));
            contentStream.showText(String.valueOf(Bill.getTotalAmount()));
            contentStream.endText();

            contentStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return document;
    }
    @Override
    public Document Receipt(Bill bill) throws FileNotFoundException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(new RectangleReadOnly(227, 842));
        PdfWriter.getInstance(document, baos);
        document.open();
        Font font = new Font(Font.FontFamily.COURIER, 12);
        Paragraph p = new Paragraph("Receipt", font);
        document.add(p);
        p = new Paragraph("-------------------------------", font);
        document.add(p);
        String title = "Order ID:" + bill.getTitle();
        p = new Paragraph(title, font);
        document.add(p);
        document.close();
        return document;
    }
}
