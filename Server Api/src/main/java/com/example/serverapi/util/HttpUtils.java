package com.example.serverapi.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpUtils {
    public static HttpHeaders getHttpHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename.concat(".xlsx"));
        return headers;
    }

    public static HttpHeaders getPdfHttpHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename.concat(".pdf"));
        return headers;
    }
}
