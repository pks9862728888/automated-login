package com.demo.automatedlogin.services;

import com.demo.automatedlogin.dao.People;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableScheduling
public class LoginService {

    private static final Object LOCK = new Object();

    private volatile String SESSION_COOKIE = "";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "toor";

    @Scheduled(fixedRate = 10000)
    void refreshCookie() {
        synchronized (LOCK) {
            this.SESSION_COOKIE = String.valueOf(UUID.randomUUID());
            System.out.println("SESSION COOKIE REFRESHED to: " + SESSION_COOKIE + " at " + LocalDateTime.now());
        }
    }

    public Optional<String> login(@NonNull String username, @NonNull String password) {
        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            System.out.println("Login successful at: " + LocalDateTime.now());
            synchronized (LOCK) {
                return Optional.of(SESSION_COOKIE);
            }
        }
        System.out.println("Login failed at: " + LocalDateTime.now());
        return Optional.empty();
    }

    public boolean checkLogin(String sessionToken) {
        synchronized (LOCK) {
            return !Objects.isNull(sessionToken) && SESSION_COOKIE.equals(sessionToken);
        }
    }

    public ResponseEntity getUnauthorizedResponse() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }
}
