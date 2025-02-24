package me.MinKyoung.springboot_developer.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter; //클래스 필드에 대해 별도 코드 없이 모든 필드에 대한 접근자 메서드를 만듦.
import lombok.NoArgsConstructor; //protected Article(){}를 대치하여 기본 생성자를 별도의 코드 없이 생성
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity //엔티티로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id //id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false)

    private Long id;

    @Column(name = "title", nullable = false)//'title'이라는 not null 컬럼과 매핑
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder //빌더 패턴(단계적(점증적)으로 생성)으로 객체 생성
    public Article(String author,String title, String content) {
        this.author=author;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
    @CreatedDate //엔티티가 생성될 때 생성 시간 저장
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate// 엔티티가 수정될 때 수정 시간 저장
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "author", nullable = false)
    private String author;



    //@Getter 애너테이션과 @NoArgsConstructor 애너테이션으로 대치됨.
    /*protected Article() { //기본 생성자

    }

    //게터
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }*/
}
