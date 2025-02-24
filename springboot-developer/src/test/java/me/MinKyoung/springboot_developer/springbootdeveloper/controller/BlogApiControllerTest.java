package me.MinKyoung.springboot_developer.springbootdeveloper.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.MinKyoung.springboot_developer.springbootdeveloper.domain.Article;
import me.MinKyoung.springboot_developer.springbootdeveloper.domain.User;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.AddArticleRequest;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.UpdateAriticleRequest;
import me.MinKyoung.springboot_developer.springbootdeveloper.repository.BlogRepository;
import me.MinKyoung.springboot_developer.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //테스트용 애플리케이션 컨텍스트
    @AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; //직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext Context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach //테스트 실행 전 실행하는 메서드
    public void mockMvcSetup() {
        this.mockMvc= MockMvcBuilders.webAppContextSetup(Context)
                .build();
        blogRepository.deleteAll();
    }
    @BeforeEach
    void setSecurityContext(){
        userRepository.deleteAll();
        user= userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        //Given, 블로그 글 추가에 필요한 요청 객체를 만듦
        final String url="/api/articles";
        final String title="title";
        final String content="content";
        final AddArticleRequest userRequest=new AddArticleRequest(title,content);

        // 객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("usernamee");

        //When, 블로그 글 추가 API에 요청을 보냄. 이때 요청 타입을 JSON이며, given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보냄
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        //Then, 응답코드가 201 Created인지 확인. Blog를 전체 조회해 크기가 1인지 확인하고, 실제 저장된 데이터와 요청 값을 비교
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();



        assertThat(articles.size()).isEqualTo(1); //크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }
    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다")
    @Test
    public void findAllArticles() throws Exception {
        //given
        final String url="/api/articles";
        Article savedArticle = createDefaultArticle();

        //when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                 .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle: 블로그 글 조회를 성공한다.")
    @Test
    public void findArticle() throws Exception {
        //given
        final String url="/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        //when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle
                .getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        //given
        final String url="/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        //when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        //then
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticles: 블로그 글 수정에 성공한다")
    @Test
    public void updateArticle() throws Exception {
        //given
        final String url="/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        final String newTitle="new Title";
        final String newContent="new Content";

        UpdateAriticleRequest request = new UpdateAriticleRequest(newTitle,newContent);
        //when
        ResultActions result = mockMvc.perform(put(url,savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }


}