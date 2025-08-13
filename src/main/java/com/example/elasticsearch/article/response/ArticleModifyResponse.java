package com.example.elasticsearch.article.response;

import com.example.elasticsearch.article.dto.ArticleDTO;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleModifyResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime modifiedDate;

    public ArticleModifyResponse(ArticleDTO articleDTO) {
        this.id = articleDTO.getId();
        this.title = articleDTO.getTitle();
        this.content = articleDTO.getContent();
        this.modifiedDate = articleDTO.getModifiedDate();
    }
}
