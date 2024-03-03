package com.example.clientapi.service;

import com.example.clientapi.dto.ReqProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelService {
    List<ReqProductDto> extractProductDtoList(MultipartFile file);
}
