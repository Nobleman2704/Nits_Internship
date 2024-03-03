package com.example.clientapi.service.impl;

import com.example.clientapi.dto.ReqProductDto;
import com.example.clientapi.service.ExcelService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

import static com.example.clientapi.util.ExcelUtil.getCellValue;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public List<ReqProductDto> extractProductDtoList(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int lastRowNum = sheet.getLastRowNum();

            List<ReqProductDto> productDtoList = new LinkedList<>();

            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);

                if (getCellValue(row.getCell(0)) == null)
                    break;

                ReqProductDto productDto = new ReqProductDto();
                productDto.setName(getCellValue(row.getCell(0)));
                productDto.setPrice(Double.valueOf(getCellValue(row.getCell(1))));
                productDto.setAmount(Double.valueOf(getCellValue(row.getCell(2))).intValue());

                productDtoList.add(productDto);
            }
            return productDtoList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
