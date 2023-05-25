package com.example.epos.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import javax.print.PrintException;
import java.io.FileNotFoundException;


public interface PosPrintService {
    void EPSONTMT20II(Document receipt) throws PrintException, DocumentException;
    void test() throws FileNotFoundException, PrintException;
}
