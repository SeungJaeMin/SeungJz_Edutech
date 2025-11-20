package com.nawbio.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired(required = false)
    private S3Client s3Client;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${cloud.aws.s3.bucket:}")
    private String bucketName;

    @Value("${cloud.aws.region.static:}")
    private String region;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 파일이 비어있는지 확인
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "파일이 비어있습니다."));
            }

            // 파일 확장자 검증
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !isImageFile(originalFilename)) {
                return ResponseEntity.badRequest().body(Map.of("error", "이미지 파일만 업로드 가능합니다."));
            }

            // 고유한 파일명 생성 (UUID + 원본 확장자)
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            String fileUrl;

            // S3 클라이언트가 설정되어 있으면 S3에 업로드 (프로덕션)
            if (s3Client != null && !bucketName.isEmpty()) {
                fileUrl = uploadToS3(file, uniqueFilename);
            } else {
                // 로컬 파일 시스템에 업로드 (개발 환경)
                fileUrl = uploadToLocal(file, uniqueFilename);
            }

            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("filename", uniqueFilename);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * S3에 이미지 업로드
     */
    private String uploadToS3(MultipartFile file, String filename) throws IOException {
        String contentType = file.getContentType();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // S3 공개 URL 반환
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, filename);
    }

    /**
     * 로컬 파일 시스템에 이미지 업로드 (개발 환경)
     */
    private String uploadToLocal(MultipartFile file, String filename) throws IOException {
        // 업로드 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장 (기존 파일이 있으면 덮어쓰기)
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // URL 반환 (프론트엔드에서 접근 가능한 경로)
        return "/api/uploads/images/" + filename;
    }

    private boolean isImageFile(String filename) {
        String lowerFilename = filename.toLowerCase();
        return lowerFilename.endsWith(".jpg") ||
               lowerFilename.endsWith(".jpeg") ||
               lowerFilename.endsWith(".png") ||
               lowerFilename.endsWith(".gif") ||
               lowerFilename.endsWith(".webp") ||
               lowerFilename.endsWith(".svg");
    }
}
