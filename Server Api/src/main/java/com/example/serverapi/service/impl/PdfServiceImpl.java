package com.example.serverapi.service.impl;

import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.service.PdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.serverapi.util.PdfUtil.getCell;

@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public byte[] downloadProducts(List<ResProductDto> dtoList) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 7f, 7f, 7f, 7f);

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
        String TNR = "fonts/times.ttf";
        String TNRB = "fonts/timesbd.ttf";

        FontFactory.register(TNR, "times regular");
        FontFactory.register(TNRB, "times bold");

        Font fontHeader18 = FontFactory.getFont("times bold", BaseFont.IDENTITY_H, true, 18);
        Font font14 = FontFactory.getFont("times bold", BaseFont.IDENTITY_H, true, 14);
        Font font12 = FontFactory.getFont("times regular", BaseFont.IDENTITY_H, true, 12);


        PdfPTable pdfPTable = new PdfPTable(9);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setWidths(new float[]{0.8f, 1.1f, 3f, 1.2f, 1.3f, 2.5f, 3f, 2.5f, 3f});


        PdfPCell headerCell = new PdfPCell(new Paragraph("Products", fontHeader18));
        headerCell.setColspan(9);
        headerCell.setPaddingBottom(10f);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        pdfPTable.addCell(headerCell);

        pdfPTable.addCell(getCell("№", font14, BaseColor.LIGHT_GRAY, 0.9f, false));
        pdfPTable.addCell(getCell("Id", font14, BaseColor.LIGHT_GRAY, 0.9f, false));
        pdfPTable.addCell(getCell("Name", font14, BaseColor.LIGHT_GRAY, 0.9f, true));
        pdfPTable.addCell(getCell("Price", font14, BaseColor.LIGHT_GRAY, 0.9f, false));
        pdfPTable.addCell(getCell("Amount", font14, BaseColor.LIGHT_GRAY, 0.9f, false));
        pdfPTable.addCell(getCell("Created", font14, BaseColor.LIGHT_GRAY, 0.9f, false));
        pdfPTable.addCell(getCell("Created By", font14, BaseColor.LIGHT_GRAY, 0.9f, true));
        pdfPTable.addCell(getCell("Updated", font14, BaseColor.LIGHT_GRAY, 0.9f, false));
        pdfPTable.addCell(getCell("Updated By", font14, BaseColor.LIGHT_GRAY, 0.9f, true));

        for (int i = 0; i < dtoList.size(); i++) {
            ResProductDto resProductDto = dtoList.get(i);

            pdfPTable.addCell(getCell(String.valueOf(i + 1), font12, BaseColor.WHITE, 0.5f, false));
            pdfPTable.addCell(getCell(resProductDto.getId().toString(), font12, BaseColor.WHITE, 0.5f, false));
            pdfPTable.addCell(getCell(resProductDto.getName(), font12, BaseColor.WHITE, 0.5f, true));
            pdfPTable.addCell(getCell(resProductDto.getPrice().toString(), font12, BaseColor.WHITE, 0.5f, false));
            pdfPTable.addCell(getCell(resProductDto.getAmount().toString(), font12, BaseColor.WHITE, 0.5f, false));
            pdfPTable.addCell(getCell(resProductDto.getCreated().toString(), font12, BaseColor.WHITE, 0.5f, false));
            pdfPTable.addCell(getCell(resProductDto.getCreatedBy(), font12, BaseColor.WHITE, 0.5f, true));
            pdfPTable.addCell(getCell(resProductDto.getUpdated().toString(), font12, BaseColor.WHITE, 0.5f, false));
            pdfPTable.addCell(getCell(resProductDto.getUpdatedBy(), font12, BaseColor.WHITE, 0.5f, true));
        }

        return pdfPTable;
    }
}
