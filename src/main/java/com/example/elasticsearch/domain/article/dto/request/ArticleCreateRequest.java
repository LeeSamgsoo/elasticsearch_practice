package com.example.elasticsearch.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
