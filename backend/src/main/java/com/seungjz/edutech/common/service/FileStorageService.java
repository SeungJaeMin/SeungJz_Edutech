package com.seungjz.edutech.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file");
        }

        // 파일 저장 경로 생성
        Path uploadPath = Paths.get(uploadDir, subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 고유한 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID().toString() + extension;

        // 파일 저장
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 상대 경로 반환 (URL로 사용)
        return "/" + subDir + "/" + filename;
    }

    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        // URL에서 파일 경로 추출 (앞의 / 제거)
        String filePath = fileUrl.startsWith("/") ? fileUrl.substring(1) : fileUrl;
        Path path = Paths.get(uploadDir, filePath);

        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    public String storeVideoFile(MultipartFile file) throws IOException {
        return storeFile(file, "videos");
    }

    public String storeThumbnailFile(MultipartFile file) throws IOException {
        return storeFile(file, "thumbnails");
    }
}
