package me.MinKyoung.springboot_developer.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.MinKyoung.springboot_developer.springbootdeveloper.domain.Article;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.AddArticleRequest;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.ArticleResponse;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.UpdateAriticleRequest;
import me.MinKyoung.springboot_developer.springbootdeveloper.service.Blogservice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    private final Blogservice blogservice;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal) {
        Article savedArticle = blogservice.save(request, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogservice.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogservice.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogservice.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                 @RequestBody UpdateAriticleRequest request) {
        Article updatedArticle = blogservice.update(id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }

}