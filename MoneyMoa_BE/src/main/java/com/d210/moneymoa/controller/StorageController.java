package com.d210.moneymoa.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.d210.moneymoa.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/file")
public class StorageController {
    @Autowired
    StorageService storageService;

    //여러파일 받기 이걸로 하나도 되면 그냥 활용
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam(value = "files") MultipartFile[] files) {
        List<String> uploadedFileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = storageService.uploadFile(file);
            uploadedFileNames.add(fileName);
        }

        return new ResponseEntity<>(uploadedFileNames, HttpStatus.OK);
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
