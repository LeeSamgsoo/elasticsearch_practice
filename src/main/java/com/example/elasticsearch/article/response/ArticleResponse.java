package com.example.elasticsearch.article.response;

import com.example.elasticsearch.article.dto.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    private final ArticleDTO articleDTO;
}
