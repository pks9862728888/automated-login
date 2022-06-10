package com.demo.automatedlogin.controllers;

import com.demo.automatedlogin.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<String> login = loginService.login(username, password);

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
