package me.MinKyoung.springboot_developer.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.MinKyoung.springboot_developer.springbootdeveloper.domain.User;
import me.MinKyoung.springboot_developer.springbootdeveloper.dto.AddUserRequest;
import me.MinKyoung.springboot_developer.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;


    public Long save(AddUserRequest dto){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public  User findById(Long userId){ //유저 ID로 유저 검색 후 전달
        return userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

}
