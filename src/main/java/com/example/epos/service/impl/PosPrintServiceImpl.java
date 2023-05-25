package com.example.epos.service.impl;

import com.example.epos.service.PosPrintService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class PosPrintServiceImpl implements PosPrintService {
    @Override
    public void EPSONTMT20II(Document receipt) throws PrintException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(receipt,baos);
        writer.flush();


        writer.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Doc pdfDoc = new SimpleDoc(bais,DocFlavor.INPUT_STREAM.AUTOSENSE,null);

        PrintService printerService = PrintServiceLookup.lookupDefaultPrintService();
        if (printerService != null) {
            DocPrintJob printJob = printerService.createPrintJob();
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new JobName("receipt", null));
            printJob.print(pdfDoc, pras);
        } else {
            System.err.println("No printers found");
        }
    }
    @Override
    public void test() throws FileNotFoundException, PrintException {
        String txtFilePath = "src/main/java/com/example/epos/service/impl/print.txt";
        FileInputStream fis = new FileInputStream(txtFilePath);
        // Use text flavor to indicate that we're printing a plain text document
        DocFlavor flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;
        Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);

        // Get all the print services
        PrintService printerService = PrintServiceLookup.lookupDefaultPrintService();

        if (printerService != null) {
            DocPrintJob printJob = printerService.createPrintJob();
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new JobName("Receipt", null));
            printJob.print(pdfDoc, pras);
        } else {
            System.err.println("Epson TM-T20II printer not found");
        }
    }
}
