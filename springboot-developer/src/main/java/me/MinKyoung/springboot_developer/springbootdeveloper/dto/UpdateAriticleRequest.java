package me.MinKyoung.springboot_developer.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateAriticleRequest {
    private String title;
    private String content;
}
