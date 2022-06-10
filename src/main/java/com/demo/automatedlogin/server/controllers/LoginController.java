package com.demo.automatedlogin.server.controllers;

import com.demo.automatedlogin.server.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestBody formData) {
        Optional<String> login = loginService.login(formData.username, formData.password);

        if (login.isEmpty()) {
            // Login failed
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Credentials");
        } else {
            // Successful login
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.COOKIE, login.get())
                    .body("Login Successful");
        }
    }
}

class LoginRequestBody {
    String username;
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
