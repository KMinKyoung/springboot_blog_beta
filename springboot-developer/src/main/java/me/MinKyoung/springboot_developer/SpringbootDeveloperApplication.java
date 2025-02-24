package me.MinKyoung.springboot_developer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //created_at, updated_at 자동 업데이트
@SpringBootApplication //필요한 기본 설정을 해줌
public class SpringbootDeveloperApplication {
//자바의 main과 같은 역할
	public static void main(String[] args) {

		SpringApplication.run(SpringbootDeveloperApplication.class, args);
	}

}
