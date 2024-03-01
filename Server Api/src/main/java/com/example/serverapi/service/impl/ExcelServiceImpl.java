package com.example.serverapi.service.impl;

import com.example.serverapi.dto.ResProductDto;
import com.example.serverapi.service.ExcelService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import static com.example.serverapi.util.ExcelUtil.createCell;
import static com.example.serverapi.util.ExcelUtil.getStyle;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public byte[] downloadProducts(List<ResProductDto> dtoList) {
        String path = "templates/products.xlsx";

        try (InputStream inputStream = new ClassPathResource(path).getInputStream();
             XSSFWorkbook wb = new XSSFWorkbook(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            XSSFSheet sheet = wb.getSheetAt(0);

            CellStyle wordStyle = getStyle(wb, 1);
            CellStyle nonWordStyle = getStyle(wb, 0);

            for (int i = 0; i < dtoList.size(); i++) {
                Row row = sheet.createRow(i + 4);
                int j = 0;
                ResProductDto productDto = dtoList.get(i);

                createCell(row, j++, nonWordStyle, i + 1);
                createCell(row, j++, nonWordStyle, productDto.getId());
                createCell(row, j++, wordStyle, productDto.getName());
                createCell(row, j++, nonWordStyle, productDto.getPrice());
                createCell(row, j++, nonWordStyle, productDto.getAmount());
                createCell(row, j++, nonWordStyle, productDto.getCreated());
                createCell(row, j++, wordStyle, productDto.getCreatedBy());
                createCell(row, j++, nonWordStyle, productDto.getUpdated());
                createCell(row, j, wordStyle, productDto.getUpdatedBy());
            }

            wb.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
