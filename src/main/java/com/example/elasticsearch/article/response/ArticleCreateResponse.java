package com.example.elasticsearch.article.response;

import com.example.elasticsearch.article.dto.ArticleDTO;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleCreateResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;

    public ArticleCreateResponse(ArticleDTO articleDTO) {
        this.id = articleDTO.getId();
        this.title = articleDTO.getTitle();
        this.content = articleDTO.getContent();
        this.createdDate = articleDTO.getCreatedDate();
    }
}
