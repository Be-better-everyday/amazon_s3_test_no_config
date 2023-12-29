package com.example.amazon_s3_test.service;


import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Value("${amazonProperties.region}")
    private String region;


    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new
                AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.Private));
    }
    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String generatePreSignedUrl(String objectKey){
        long expirationTimeMillis = 3600000;
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));

        // Generate the pre-signed URL
        String preSignedUrl = s3client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        return preSignedUrl;
    }

    public String generatePreSignedUrlFromStackOverFlow (String objecKey){

        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objecKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        generatePresignedUrlRequest.addRequestParameter("response-content-disposition", "inline");
        generatePresignedUrlRequest.addRequestParameter("X-Amz-Security-Token", "YourSecurityToken"); // Replace with the actual security token

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        System.out.println("Pre-Signed URL: " + url.toString());
        return url.toString();
    }

    //  https://stackoverflow.com/questions/50980867/generate-a-pre-signed-url-using-the-aws-sdk-for-java
//    public String generatePreSignedUrlFromStackOverFlow (String objecKey){
//        java.util.Date expiration = new java.util.Date();
//        long expTimeMillis = expiration.getTime();
//        expTimeMillis += 1000 * 60 * 60;
//        expiration.setTime(expTimeMillis);
//
//        GeneratePresignedUrlRequest generatePresignedUrlRequest =
//                new GeneratePresignedUrlRequest(bucketName, objecKey)
//                        .withMethod(HttpMethod.GET)
//                        .withExpiration(expiration);
//        generatePresignedUrlRequest.addRequestParameter("response-content-disposition", "inline");
//        generatePresignedUrlRequest.addRequestParameter("X-Amz-Security-Token", "YourSecurityToken"); // Replace with the actual security token
//
//        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
//        System.out.println("Pre-Signed URL: " + url.toString());
//        return url.toString();
//    }



    /* Create a pre-signed URL to download an object in a subsequent GET request. */
//    public String createPresignedGetUrl(String bucketName, String keyName) {
//        try (S3Presigner presigner = S3Presigner.create()) {
//
//            GetObjectRequest objectRequest = GetObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(keyName)
//                    .build();
//
//            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                    .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
//                    .getObjectRequest(objectRequest)
//                    .build();
//
//            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
//            logger.info("Presigned URL: [{}]", presignedRequest.url().toString());
//            logger.info("HTTP method: [{}]", presignedRequest.httpRequest().method());
//
//            return presignedRequest.url().toExternalForm();
//        }
//    }
}
