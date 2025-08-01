package com.raxrot.back.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FileUploadServiceImplTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private FileUploadServiceImpl fileUploadService;

    @Value("${aws.bucket.name}")
    private String bucketName = "test-bucket";

    @Test
    void uploadFile_shouldReturnImageUrl_whenUploadSuccessful() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "test".getBytes());

        PutObjectResponse putObjectResponse = mock(PutObjectResponse.class);
        SdkHttpResponse sdkHttpResponse = mock(SdkHttpResponse.class);

        when(sdkHttpResponse.isSuccessful()).thenReturn(true);
        when(putObjectResponse.sdkHttpResponse()).thenReturn(sdkHttpResponse);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(putObjectResponse);

        String result = fileUploadService.uploadFile(file);

        assertTrue(result.contains("https://"));
        assertTrue(result.contains(".s3.amazonaws.com/"));
    }

    @Test
    void deleteFile_shouldCallS3ClientDeleteObject() {
        String imageUrl = "https://test-bucket.s3.amazonaws.com/abc123.jpg";

        fileUploadService.deleteFile(imageUrl);

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}
