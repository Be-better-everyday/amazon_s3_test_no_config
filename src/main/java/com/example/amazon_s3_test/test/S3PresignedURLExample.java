//package com.example.amazon_s3_test.test;
//
//import com.amazonaws.HttpMethod;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
//
//import java.util.Arrays;
//import java.util.Date;
//
//public class S3PresignedURLExample {
//
//    public static void main(String[] args) {
//        // Replace these with your own values
//        String objectUrl = "https://s3.ap-southeast-1.amazonaws.com/spring4sem2/1703827214371-Screenshot_6.png";
//        String awsRegion = "ap-southeast-1";
//
//        // Extract bucket name and object key from the object URL
//        String[] urlParts = objectUrl.replace("https://", "").split("/");
//        String bucketName = urlParts[1];
//        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 2, urlParts.length));
//
//        // Create an S3 client
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
//
//        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
////                .basicCre
//                .withRegion(awsRegion)
//                .build();
//
//        // Set the expiration time for the pre-signed URL (in milliseconds)
//        long expirationTimeMillis = 3600000; // 1 hour
//
//         Create a GeneratePresignedUrlRequest
//        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
//                .withMethod(HttpMethod.GET)
//                .withExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));
//
//        // Generate the pre-signed URL
//        String preSignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
//
//        // Print the pre-signed URL
//        System.out.println("Pre-signed URL: " + preSignedUrl);
//    }
//}
