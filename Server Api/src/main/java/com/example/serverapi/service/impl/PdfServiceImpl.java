package com.example.serverapi.service.impl;

import com.example.serverapi.dto.ResProductDto;
import com.example.serverapi.service.PdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public byte[] downloadProducts(List<ResProductDto> dtoList) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Document document = new Document(PageSize.A4.rotate());

            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            PdfPTable pdfPTable = getProductTable(dtoList);

            document.add(pdfPTable);

            document.close();
            writer.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PdfPTable getProductTable(List<ResProductDto> dtoList) throws DocumentException {
        PdfPTable pdfPTable = new PdfPTable(9);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setWidths(new float[]{1f, 1.3f, 3f, 1.5f, 1f, 2f, 3f, 2f, 3f});

        Font fontHead = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, true, 16);

        PdfPCell cellN = new PdfPCell(new Paragraph("№"));

        cellN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellN.setVerticalAlignment(Element.ALIGN_MIDDLE);



        return null;
    }
}
