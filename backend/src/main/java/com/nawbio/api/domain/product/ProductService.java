package com.nawbio.api.domain.product;

import com.nawbio.api.common.exception.ResourceNotFoundException;
import com.nawbio.api.domain.product.dto.ProductCreateDto;
import com.nawbio.api.domain.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDto> getAllProducts(String category, String lang) {
        List<Product> products;
        String languageCode = lang != null ? lang : "ko";

        if (category != null && !category.isEmpty() && !"전체".equals(category)) {
            products = productRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrder(category);
        } else {
            products = productRepository.findByIsActiveTrueOrderByDisplayOrder();
        }

        return products.stream()
                .map(product -> ProductDto.from(product, languageCode))
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id, String lang) {
        String languageCode = lang != null ? lang : "ko";
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductDto.from(product, languageCode);
    }

    @Transactional
    public ProductDto createProduct(ProductCreateDto dto) {
        Product product = Product.builder()
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .bgColor(dto.getBgColor() != null ? dto.getBgColor() : "#6B8E23")
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .isActive(true)
                .build();

        // 번역 데이터 추가
        if (dto.getTranslations() != null) {
            for (Map.Entry<String, ProductCreateDto.TranslationData> entry : dto.getTranslations().entrySet()) {
                String langCode = entry.getKey();
                ProductCreateDto.TranslationData transData = entry.getValue();

                ProductTranslation translation = ProductTranslation.builder()
                        .languageCode(langCode)
                        .name(transData.getName())
                        .subtitle(transData.getSubtitle())
                        .description(transData.getDescription())
                        .usage(transData.getUsage())
                        .features(transData.getFeatures())
                        .benefits(transData.getBenefits())
                        .specifications(transData.getSpecifications())
                        .build();

                product.addTranslation(translation);
            }
        }

        Product saved = productRepository.save(product);
        return ProductDto.from(saved, "ko");
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductCreateDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());
        if (dto.getBgColor() != null) {
            product.setBgColor(dto.getBgColor());
        }
        if (dto.getDisplayOrder() != null) {
            product.setDisplayOrder(dto.getDisplayOrder());
        }

        // 기존 번역 삭제 후 재생성
        product.getTranslations().clear();

        // 새 번역 데이터 추가
        if (dto.getTranslations() != null) {
            for (Map.Entry<String, ProductCreateDto.TranslationData> entry : dto.getTranslations().entrySet()) {
                String langCode = entry.getKey();
                ProductCreateDto.TranslationData transData = entry.getValue();

                ProductTranslation translation = ProductTranslation.builder()
                        .languageCode(langCode)
                        .name(transData.getName())
                        .subtitle(transData.getSubtitle())
                        .description(transData.getDescription())
                        .usage(transData.getUsage())
                        .features(transData.getFeatures())
                        .benefits(transData.getBenefits())
                        .specifications(transData.getSpecifications())
                        .build();

                product.addTranslation(translation);
            }
        }

        Product updated = productRepository.save(product);
        return ProductDto.from(updated, "ko");
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setIsActive(false);
        productRepository.save(product);
    }
}
