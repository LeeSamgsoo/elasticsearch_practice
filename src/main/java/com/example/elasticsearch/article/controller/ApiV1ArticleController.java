package com.example.elasticsearch.article.controller;

import com.example.elasticsearch.article.dto.ArticleDTO;
import com.example.elasticsearch.article.entity.Article;
import com.example.elasticsearch.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ApiV1ArticleController {
    private final ArticleService articleService;

    @GetMapping("")
    public List<ArticleDTO> getArticles() {
        return this.articleService.getArticles();
    }

    @GetMapping("/{id}")
    public ArticleDTO getArticle(@PathVariable(value = "id") Long id) {
        return this.articleService.getArticle(id);
    }

    @PostMapping("")
    public ArticleDTO createArticle(@RequestParam("title") String title,
                                    @RequestParam("content") String content) {
        return this.articleService.articleCreate(title, content);
    }

    @PatchMapping("/{id}")
    public ArticleDTO modifyArticle(@PathVariable(value = "id") Long id,
                                    @RequestParam("title") String title,
                                    @RequestParam("content") String content) {
        return this.articleService.articleModify(id, title, content);
    }

    @DeleteMapping("/{id}")
    public ArticleDTO deleteArticle(@PathVariable(value = "id") Long id) {
        return this.articleService.articleDelete(id);
    }
}
