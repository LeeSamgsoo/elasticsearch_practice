package com.example.elasticsearch.article.controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.example.elasticsearch.article.dto.ArticleDTO;
import com.example.elasticsearch.article.request.ArticleCreateRequest;
import com.example.elasticsearch.article.request.ArticleModifyRequest;
import com.example.elasticsearch.article.response.*;
import com.example.elasticsearch.article.service.ArticleService;
import com.example.elasticsearch.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
                new ArticlesResponse(articleDTOList.stream().map(ArticleResponse::new).collect(Collectors.toList()))
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
    public RsData<ArticleCreateResponse> createArticle(@Valid @RequestBody ArticleCreateRequest articleCreateRequest) {
        ArticleDTO articleDTO = this.articleService.articleCreate(
                articleCreateRequest.getTitle(),
                articleCreateRequest.getContent()
        );
        return RsData.of(
                "201",
                "게시글 생성 성공",
                new ArticleCreateResponse(articleDTO)
        );
    }

    @PatchMapping("/{id}")
    public RsData<ArticleModifyResponse> modifyArticle(@PathVariable(value = "id") Long id,
                                                                @Valid @RequestBody ArticleModifyRequest articleModifyRequest) {
        ArticleDTO articleDTO = this.articleService.articleModify(
                id,
                articleModifyRequest.getTitle(),
                articleModifyRequest.getContent()
        );
        if (articleDTO == null) {
            return RsData.of(
                    "401",
                    "존재하지 않는 게시글 입니다."
            );
        }
        return RsData.of(
                "200",
                "게시글이 수정되었습니다.",
                new ArticleModifyResponse(articleDTO)
        );
    }

    @DeleteMapping("/{id}")
    public RsData<ArticleDeleteResponse> deleteArticle(@PathVariable(value = "id") Long id) {
        ArticleDTO articleDTO = this.articleService.articleDelete(id);
        if (articleDTO == null) {
            return RsData.of(
                    "400",
                    "존재하지 않는 게시글 입니다."
            );
        }
        return RsData.of(
                "200",
                "게시글이 삭제되었습니다.",
                new ArticleDeleteResponse(articleDTO)
        );
    }
}
