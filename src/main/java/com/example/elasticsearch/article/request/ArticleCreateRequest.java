package com.example.elasticsearch.article.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
