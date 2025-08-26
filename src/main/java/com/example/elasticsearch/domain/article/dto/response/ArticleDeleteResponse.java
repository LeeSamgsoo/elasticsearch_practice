package com.example.elasticsearch.domain.article.dto.response;

import com.example.elasticsearch.domain.article.dto.ArticleDTO;
import lombok.Getter;

@Getter
public class ArticleDeleteResponse {
    private final Long id;
    private final String title;
    private final String content;

    public ArticleDeleteResponse(ArticleDTO articleDTO) {
        this.id = articleDTO.getId();
        this.title = articleDTO.getTitle();
        this.content = articleDTO.getContent();
    }
}
