package com.example.elasticsearch.domain.article.dto.response;

import com.example.elasticsearch.domain.article.dto.ArticleDTO;
import lombok.Getter;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;

    public ArticleResponse(ArticleDTO articleDTO) {
        this.title = articleDTO.getTitle();
        this.content = articleDTO.getContent();
    }
}
