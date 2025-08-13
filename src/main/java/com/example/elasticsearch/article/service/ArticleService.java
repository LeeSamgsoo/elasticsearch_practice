package com.example.elasticsearch.article.service;

import com.example.elasticsearch.article.dto.ArticleDTO;
import com.example.elasticsearch.article.entity.Article;
import com.example.elasticsearch.article.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<ArticleDTO> getArticles() {
        return this.articleRepository.findAll().stream().map(ArticleDTO::new).collect(Collectors.toList());
    }

    public ArticleDTO getArticle(Long id) {
        return this.articleRepository.findById(id).map(ArticleDTO::new).orElse(null);
    }

    public ArticleDTO articleCreate(String title, String content) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .build();
        this.articleRepository.save(article);
        return new ArticleDTO(article);
    }

    public ArticleDTO articleModify(Long id, String title, String content) {
        Article article = this.articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new RuntimeException("존재하지 않는 게시글 입니다.");
        }
        Article updateArticle = article.toBuilder()
                .title(title)
                .content(content)
                .build();
        this.articleRepository.save(updateArticle);
        return new ArticleDTO(updateArticle);
    }

    public ArticleDTO articleDelete(Long id) {
        Article article = this.articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new RuntimeException("존재하지 않는 게시글 입니다.");
        }
        this.articleRepository.delete(article);
        return new ArticleDTO(article);
    }
}
