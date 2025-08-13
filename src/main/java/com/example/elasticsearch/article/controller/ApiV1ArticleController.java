package com.example.elasticsearch.article.controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.example.elasticsearch.article.dto.ArticleDTO;
import com.example.elasticsearch.article.response.ArticleResponse;
import com.example.elasticsearch.article.response.ArticlesResponse;
import com.example.elasticsearch.article.service.ArticleService;
import com.example.elasticsearch.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ApiV1ArticleController {
    private final ArticleService articleService;

    @GetMapping("")
    public RsData<ArticlesResponse> getArticles() {
        List<ArticleDTO> articleDTOList = this.articleService.getArticles();
        if (articleDTOList.isEmpty()) {
            return RsData.of(
                    "401",
                    "게시글이 존재하지 않습니다."
            );
        }
        return RsData.of(
                "200",
                "다건 조회 성공",
                new ArticlesResponse(articleDTOList)
        );
    }

    @GetMapping("/{id}")
    public RsData<ArticleResponse> getArticle(@PathVariable(value = "id") Long id) {
        ArticleDTO articleDTO = this.articleService.getArticle(id);
        if (articleDTO == null) {
            return RsData.of(
              "401",
              "존재하지 않는 게시글 입니다."
            );
        }
        return RsData.of(
                "200",
                "단건 조회 성공",
                new ArticleResponse(articleDTO)
        );
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
