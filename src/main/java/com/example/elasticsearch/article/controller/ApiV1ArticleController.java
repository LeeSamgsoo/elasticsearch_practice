package com.example.elasticsearch.article.controller;

import com.example.elasticsearch.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ApiV1ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public String getArticles() {
        return "목록";
    }

    @GetMapping
    public String getArticle() {
        return "단건";
    }

    @PostMapping
    public String createArticle() {
        return "생성";
    }

    @PatchMapping
    public String modifyArticle() {
        return "수정";
    }

    @DeleteMapping
    public String deleteArticle() {
        return "삭제";
    }
}
