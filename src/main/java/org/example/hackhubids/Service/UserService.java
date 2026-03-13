package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.User;
import org.example.hackhubids.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    @Transactional
    public User register(String name, String email, String password) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalStateException("Email already registered");
        });
        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }
}
