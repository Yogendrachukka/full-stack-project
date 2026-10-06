package com.foodbridge.controller;

import com.foodbridge.model.User;
import com.foodbridge.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    record RegisterRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,
        String role
    ) {}

    record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (users.findByEmailIgnoreCase(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(encoder.encode(request.password()));

        try {
            user.setRole(User.Role.valueOf(
                request.role() == null ? "DONOR" : request.role().toUpperCase()
            ));
        } catch (IllegalArgumentException e) {
            user.setRole(User.Role.DONOR);
        }

        users.save(user);
        return ResponseEntity.ok(Map.of(
            "message", "Account created",
            "name", user.getName(),
            "role", user.getRole()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        User user = users.findByEmailIgnoreCase(request.email()).orElse(null);

        if (user == null || !encoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }

        session.setAttribute("userId", user.getId());

        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "userId", user.getId(),
            "name", user.getName(),
            "role", user.getRole()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
