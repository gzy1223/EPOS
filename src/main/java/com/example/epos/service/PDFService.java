package com.example.epos.service;

import com.example.epos.entity.Bill;
import com.example.epos.entity.Orders;
import com.example.epos.entity.Restaurant;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.FileNotFoundException;

public interface PDFService {
    PDDocument RegisterPDF(Restaurant restaurant);
    PDDocument PasswordPDF(String password);
    void PrintPDF(String filePath);
    Document Receipt(Bill bill) throws FileNotFoundException, DocumentException;
    PDDocument OrderPDF(Bill bill);
}
