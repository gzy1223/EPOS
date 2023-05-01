package com.example.epos.service;

import com.example.epos.entity.Bill;
import com.example.epos.entity.Orders;
import com.example.epos.entity.Restaurant;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface PDFService {
    PDDocument RegisterPDF(Restaurant restaurant);
    PDDocument PasswordPDF(String password);

    PDDocument OrderPDF(Bill bill);
}
