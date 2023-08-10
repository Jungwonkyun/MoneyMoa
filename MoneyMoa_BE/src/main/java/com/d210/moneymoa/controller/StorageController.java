package com.d210.moneymoa.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.d210.moneymoa.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;


@RestController
@RequestMapping("/api/file")
public class StorageController {
    @Autowired
    StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file){
        return new ResponseEntity<>(storageService.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<String> downloadFile(@PathVariable String fileName) {
        try {
            String base64ImageData = storageService.downloadFileAsBase64(fileName);
            return ResponseEntity.ok().body(base64ImageData);

        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                // 파일이 존재하지 않는 경우를 처리하는 코드
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested file not found.");
            } else {
                // 기타 S3 에러 처리 코드
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
            }
        }
    }

    @GetMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return new ResponseEntity<>(storageService.deleteFile(fileName), HttpStatus.OK);
    }

}
