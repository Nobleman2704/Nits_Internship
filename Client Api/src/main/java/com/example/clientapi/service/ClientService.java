package com.example.clientapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface ClientService {
    void sendProducts(String token, MultipartFile file);
}
