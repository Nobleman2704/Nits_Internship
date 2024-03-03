package com.example.clientapi.service.impl;

import com.example.clientapi.dto.ReqProductDto;
import com.example.clientapi.dto.ResponseDto;
import com.example.clientapi.service.ClientService;
import com.example.clientapi.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    @Qualifier(value = "restTemplateServerApi")
    private final RestTemplate restTemplateServerApi;
    private final ExcelService excelService;


    @Override
    public void sendProducts(String token, MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        if (contentType == null || fileName == null ||
            !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
            (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")))
            throw new RuntimeException("Excel file required");

        List<ReqProductDto> productDtoList = excelService.extractProductDtoList(file);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        productDtoList.forEach(reqProductDto -> {
            executorService.execute(() -> {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token);

                HttpEntity<ReqProductDto> httpEntity = new HttpEntity<>(reqProductDto, headers);

                String url = "/api/product/create";

                try {
                    ResponseEntity<ResponseDto> response = restTemplateServerApi.exchange(
                            url,
                            HttpMethod.POST,
                            httpEntity,
                            ResponseDto.class
                    );
                    ResponseDto responseDto = response.getBody();
                    System.out.println("Thread name:" + Thread.currentThread().getName()
                                       + ", response = " + responseDto.getData());
                } catch (Exception e) {
                    log.error("Thread name:" + Thread.currentThread().getName() +
                              ", error -> " + e.getMessage());
                }
            });
        });

        executorService.shutdown();
    }
}
