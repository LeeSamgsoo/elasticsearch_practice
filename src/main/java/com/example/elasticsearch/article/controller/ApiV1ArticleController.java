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
        List<ArticleDTO> articleDTOList = new ArrayList<>();

        Article article1 = new Article("제목1", "내용1");
        Article article2 = new Article("제목2", "내용2");
        Article article3 = new Article("제목3", "내용3");

        articleDTOList.add(new ArticleDTO(article1));
        articleDTOList.add(new ArticleDTO(article2));
        articleDTOList.add(new ArticleDTO(article3));

        return articleDTOList;
    }

    @GetMapping("/{id}")
    public ArticleDTO getArticle(@PathVariable(value = "id") Long id) {
        return new ArticleDTO(new Article("제목1", "내용1"));
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
