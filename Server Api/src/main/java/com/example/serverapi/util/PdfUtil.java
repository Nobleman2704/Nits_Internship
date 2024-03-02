package com.example.serverapi.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfUtil {
    public static PdfPCell getCell(String text, Font font, BaseColor baseColor, float borderWith, boolean isWordCell) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(baseColor);
        cell.setBorderWidth(borderWith);
        cell.setHorizontalAlignment(isWordCell ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}
