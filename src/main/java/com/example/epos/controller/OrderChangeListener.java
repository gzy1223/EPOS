package com.example.epos.controller;

import com.example.epos.entity.Bill;
import com.example.epos.service.PDFService;
import com.example.epos.service.PosPrintService;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.print.PrintException;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

@Component
public class OrderChangeListener {
    @Autowired
    private PosPrintService posPrintService;
    @Autowired
    private PDFService pdfService;
    private String orderTime;
//    private Firestore db;
//
//    public OrderChangeListener()
//    {
//        db = FirestoreClient.getFirestore();
//    }

    @Scheduled(fixedRate = 5000)
    public void checkForNewOrders() throws ExecutionException, InterruptedException, DocumentException, FileNotFoundException, PrintException {
        Firestore db = FirestoreClient.getFirestore();
        //get the order
        QuerySnapshot snapshot = db.collection("orders").get().get();
        Bill bill = new Bill();
        bill.setTitle("test restaurant");
        //posPrintService.EPSONTMT20II(pdfService.Receipt(bill));
        posPrintService.test();
        //
        System.out.println("test");
    }
}
