package com.demo.automatedlogin.client.services;

import com.demo.automatedlogin.client.exceptions.RequestException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;
import java.util.Optional;

@Service
public class RequestInterceptorService {

    @Autowired
    private RestAssuredService restAssuredService;

    @Autowired
    private ClientLoginService clientLoginService;

    public Response makeRequest(
            @NonNull String url, @Nullable String payload,
            @NonNull RequestMethod requestMethod, @Nullable String cookie) throws RequestException {
        int retryCount = 0;
        int maxRetryCount = 1;
        Response response = null;

        // Request interception logic which takes care of expired login
        while (true) {
            switch (requestMethod) {
                case GET:
                    response = restAssuredService.makeGetRequest(url, cookie);
                    break;
                case POST:
                    response = restAssuredService.makePostRequest(Objects.requireNonNull(payload), url, cookie);
                    break;
                case PUT:
                    break;
                case PATCH:
                    break;
                case DELETE:
                    break;
                default:
                    throw new RequestException("Unknown Request method: " + requestMethod);
            }

            // Check if login expired (401)
            if (isLoginExpired(response)) {
                System.out.println("LOGIN EXPIRED!");
                retryCount++;
                if (retryCount > maxRetryCount) {
                    break;
                }

                // Login
                Optional<String> sessionCookie = clientLoginService.login();
                if (sessionCookie.isEmpty()) {
                    // Login failed
                    break;
                } else {
                    // Login was successful
                    cookie = sessionCookie.get();
                }
            } else {
                break;
            }
        }

        return response;
    }

    private boolean isLoginExpired(Response response) {
        return !Objects.isNull(response) && response.statusCode() == HttpStatus.SC_UNAUTHORIZED;
    }

}
