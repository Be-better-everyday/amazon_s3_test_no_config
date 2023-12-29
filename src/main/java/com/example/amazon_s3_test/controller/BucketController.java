package com.example.amazon_s3_test.controller;

import com.example.amazon_s3_test.service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/storage")
public class BucketController {

    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    //  1703833081586-Screenshot_6.png
    @GetMapping("")
    public String getPreSignedUrl(String objectKey){
        return amazonClient.generatePreSignedUrlFromStackOverFlow(objectKey);
    }
}
