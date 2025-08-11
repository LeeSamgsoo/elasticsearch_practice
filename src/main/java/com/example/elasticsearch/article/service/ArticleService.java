package com.example.elasticsearch.article.service;

import com.example.elasticsearch.article.dto.ArticleDTO;
import com.example.elasticsearch.article.entity.Article;
import com.example.elasticsearch.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleDTO articleCreate(String title, String content) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .build();
        this.articleRepository.save(article);
        return new ArticleDTO(article);
    }
}
