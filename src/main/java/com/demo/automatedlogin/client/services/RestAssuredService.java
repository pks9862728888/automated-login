package com.demo.automatedlogin.client.services;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.demo.automatedlogin.client.services.ClientLoginService.COOKIE_NAME;
import static io.restassured.RestAssured.given;

@Service
public class RestAssuredService {

    private RequestSpecification getRequestSpecification() {
        return given()
                .contentType(ContentType.JSON);
    }

    public Response makePostRequest(@NonNull String payload, @NonNull String url, @Nullable String cookie) {
        RequestSpecification requestSpecification = getRequestSpecification();
        if (Objects.nonNull(cookie)) {
            requestSpecification.header(COOKIE_NAME, cookie);
        }
        return requestSpecification
                .body(payload.getBytes(StandardCharsets.UTF_8))
                .post(url);
    }

    public Response makeGetRequest(@NonNull String url, @Nullable String cookie) {
        RequestSpecification requestSpecification = getRequestSpecification();
        if (Objects.nonNull(cookie)) {
            requestSpecification.cookie(COOKIE_NAME, cookie);
        }
        return requestSpecification
                .get(url);
    }
}
