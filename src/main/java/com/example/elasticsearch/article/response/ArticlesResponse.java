package com.example.elasticsearch.article.response;

import com.example.elasticsearch.article.dto.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticlesResponse {
    private final List<ArticleDTO> articleDTOList;
}
