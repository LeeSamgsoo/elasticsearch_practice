package com.example.elasticsearch.article.controller;

import com.example.elasticsearch.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ApiV1ArticleController {
    private final ArticleService articleService;


    @GetMapping("")
    public String getArticles() {
        return "목록";
    }

    @GetMapping("/{id}")
    public String getArticle(@PathVariable(value = "id") Long id) {
        return "단건";
    }

    @PostMapping("")
    public String createArticle() {
        return "생성";
    }

    @PatchMapping("/{id}")
    public String modifyArticle(@PathVariable(value = "id") Long id) {
        return "수정";
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable(value = "id") Long id) {
        return "삭제";
    }
}
