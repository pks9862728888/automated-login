package com.demo.automatedlogin.client.controllers;

import com.demo.automatedlogin.client.exceptions.RequestException;
import com.demo.automatedlogin.client.services.ClientLoginService;
import com.demo.automatedlogin.client.services.RequestInterceptorService;
import com.demo.automatedlogin.client.services.RequestPayloadCreatorService;
import com.demo.automatedlogin.client.services.RestAssuredService;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private RequestInterceptorService requestInterceptorService;

    @Autowired
    private RestAssuredService restAssuredService;

    @Autowired
    private ClientLoginService clientLoginService;

    @Autowired
    private RequestPayloadCreatorService requestPayloadCreatorService;

    @Value("${baseUrl}people/list")
    private String listAllPeopleUrl;

    @Value("${baseUrl}people/create")
    private String createPeopleUrl;

    @GetMapping("/list-all-people")
    public ResponseEntity<String> listAllPeople() throws RequestException {
        // Client testing code will make request to the generic interceptor method to make rest call
        // Interceptor will return response object after 1 retry
        Response response = requestInterceptorService.makeRequest(
                listAllPeopleUrl,
                null,
                RequestMethod.GET,
                clientLoginService.getSessionCookie()
        );

        // After getting the response object from interceptor method, do whatever validation you want here
        // In this case, I don't want to test anything, so I am returning whatever we got to the rest call
        return ResponseEntity.status(response.statusCode())
                .body(response.getBody().asString());
    }

    @GetMapping("/create")
    public ResponseEntity<String> create() throws RequestException {
        // Client testing code will make request to the generic interceptor method to make rest call
        // Interceptor will return response object after 1 retry
        Response response = requestInterceptorService.makeRequest(
                createPeopleUrl,
                requestPayloadCreatorService.getCreatePeoplePayload(
                        RandomStringUtils.randomAlphabetic(10),  // Random people name
                        new Random().nextInt(100)                // Random age
                ),
                RequestMethod.POST,
                clientLoginService.getSessionCookie()
        );

        // After getting the response object from interceptor method, do whatever validation you want here
        // In this case, I don't want to test anything, so I am returning whatever we got to the rest call
        return ResponseEntity.status(response.statusCode())
                .body(response.getBody().asString());
    }

}
