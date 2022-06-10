package com.demo.automatedlogin.client.services;

import com.demo.automatedlogin.client.exceptions.RequestException;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientLoginService {

    private final Object LOCK = new Object();

    private volatile String CLIENT_SESSION_COOKIE = "";

    public static final String COOKIE_NAME = "Cookie";

    @Value("${username}")
    private String USERNAME;

    @Value("${password}")
    private String PASSWORD;

    @Value("${baseUrl}login")
    private String loginUrl;

    @Autowired
    private RestAssuredService restAssuredService;

    @Autowired
    private RequestPayloadCreatorService requestPayloadCreatorService;

    /**
     * Tries to login, if successful returns the cookie, else returns empty Optional
     */
    public Optional<String> login() {
        synchronized (LOCK) {
            System.out.println("Trying to login...");
            try {
                Response response = restAssuredService.makePostRequest(
                        requestPayloadCreatorService.getLoginRequestPayload(USERNAME, PASSWORD),
                        loginUrl,
                        null
                );

                if (response.statusCode() == HttpStatus.SC_OK) {
                    System.out.println("Login successful!");
                    this.CLIENT_SESSION_COOKIE = response.getHeader(COOKIE_NAME);
                    return Optional.of(this.CLIENT_SESSION_COOKIE);
                }
            } catch (RequestException e) {
                e.printStackTrace();
                System.out.println("Login Failed!");
            }
            return Optional.empty();
        }
    }

    public String getSessionCookie() {
        synchronized (LOCK) {
            return this.CLIENT_SESSION_COOKIE;
        }
    }

}
