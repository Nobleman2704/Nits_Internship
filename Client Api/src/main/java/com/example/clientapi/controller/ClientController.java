package com.example.clientapi.controller;

import com.example.clientapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "CLIENT-API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client-api")
public class ClientController {
    private final ClientService clientService;

    @Operation(summary = "sending only excel file with token")
    @PostMapping("/send-file")
    public void sendFile(@RequestParam MultipartFile file,
                         @RequestParam String token) {

        clientService.sendProducts(token, file);
    }

}
