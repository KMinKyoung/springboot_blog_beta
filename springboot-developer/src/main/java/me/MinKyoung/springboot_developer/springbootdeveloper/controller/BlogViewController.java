package me.MinKyoung.springboot_developer.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.MinKyoung.springboot_developer.springbootdeveloper.domain.Article;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.ArticleListViewResponse;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.ArticleViewResponse;
import me.MinKyoung.springboot_developer.springbootdeveloper.service.Blogservice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final Blogservice blogservice;
    
    @GetMapping("/articles") //블로그 글 확인
    public String getArticles(Model model) {
        List<ArticleListViewResponse> arricles = blogservice.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", arricles); //블로그 글 리스트 저장
        
        return "articleList"; //.html뷰 조회
    }

    @GetMapping("/articles/{id}") //글 상세 조회
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogservice.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }
    @GetMapping("new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if(id == null) { //id가 없으면 생성
            model.addAttribute("article", new ArticleViewResponse());
        }else { //id가 없으면 수정
            Article article = blogservice.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
