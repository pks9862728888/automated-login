package com.demo.automatedlogin.server.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

    @Value("${username}")
    private String USERNAME;

    @Value("${password}")
    private String PASSWORD;

    private volatile String SESSION_COOKIE = "";

    @Scheduled(fixedRate = 20000)
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

    public boolean isLoginExpired(@Nullable String sessionToken) {
        synchronized (LOCK) {
            if (Objects.nonNull(sessionToken) && sessionToken.contains("Cookie=")) {
                sessionToken = sessionToken.substring(Math.max(0, sessionToken.indexOf("Cookie=") + 7));
            }
            return Objects.nonNull(sessionToken) && SESSION_COOKIE.equals(sessionToken);
        }
    }

    public ResponseEntity getUnauthorizedResponse() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }
}
