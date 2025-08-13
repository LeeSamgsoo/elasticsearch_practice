package com.example.elasticsearch.article.controller;

import com.example.elasticsearch.article.dto.ArticleDTO;
import com.example.elasticsearch.article.request.ArticleCreateRequest;
import com.example.elasticsearch.article.request.ArticleModifyRequest;
import com.example.elasticsearch.article.response.*;
import com.example.elasticsearch.article.service.ArticleService;
import com.example.elasticsearch.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/articles", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1ArticleController", description = "게시물 CRUD API")
public class ApiV1ArticleController {
    private final ArticleService articleService;

    @GetMapping("")
    @Operation(summary = "게시글 다건 조회")
    public RsData<ArticlesResponse> getArticles() {
        List<ArticleDTO> articleDTOList = this.articleService.getArticles();
        if (articleDTOList.isEmpty()) {
            return RsData.of(
                    "500",
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
    @Operation(summary = "게시글 단건 조회")
    public RsData<ArticleResponse> getArticle(@PathVariable(value = "id") Long id) {
        ArticleDTO articleDTO = this.articleService.getArticle(id);
        if (articleDTO == null) {
            return RsData.of(
                    "500",
                    "%d번 게시글은 존재하지 않습니다.".formatted(id)
            );
        }
        return RsData.of(
                "200",
                "단건 조회 성공",
                new ArticleResponse(articleDTO)
        );
    }

    @PostMapping("")
    @Operation(summary = "게시글 생성")
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
    @Operation(summary = "게시글 수정")
    public RsData<ArticleModifyResponse> modifyArticle(@PathVariable(value = "id") Long id,
                                                       @Valid @RequestBody ArticleModifyRequest articleModifyRequest) {
        ArticleDTO articleDTO = this.articleService.articleModify(
                id,
                articleModifyRequest.getTitle(),
                articleModifyRequest.getContent()
        );
        if (articleDTO == null) {
            return RsData.of(
                    "500",
                    "%d번 게시글은 존재하지 않습니다.".formatted(id)
            );
        }
        return RsData.of(
                "200",
                "게시글이 수정되었습니다.",
                new ArticleModifyResponse(articleDTO)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제")
    public RsData<ArticleDeleteResponse> deleteArticle(@PathVariable(value = "id") Long id) {
        ArticleDTO articleDTO = this.articleService.articleDelete(id);
        if (articleDTO == null) {
            return RsData.of(
                    "500",
                    "%d번 게시글은 존재하지 않습니다.".formatted(id)
            );
        }
        return RsData.of(
                "200",
                "게시글이 삭제되었습니다.",
                new ArticleDeleteResponse(articleDTO)
        );
    }
}
