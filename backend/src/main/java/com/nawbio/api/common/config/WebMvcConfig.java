package com.nawbio.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드된 이미지를 정적 리소스로 제공
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().toString();

        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
