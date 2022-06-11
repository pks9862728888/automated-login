package com.demo.automatedlogin.client.services;

import com.demo.automatedlogin.client.exceptions.RequestException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RequestPayloadCreatorService {

    @Autowired
    private FileService fileService;

    public String getLoginRequestPayload(
            @NonNull String username, @NonNull String password) throws RequestException {
        return String.format(getPayloadElseFail("LoginPayload.json"),
                username, password);
    }

    public String getCreatePeoplePayload(@NonNull String name, int age) throws RequestException {
        return String.format(getPayloadElseFail("createPeoplePayload.json"),
                name, age);
    }

    private String getPayloadElseFail(@NonNull String payloadFileName) throws RequestException {
        try {
            return fileService.getJsonRequestPayload(payloadFileName);
        } catch (IOException e) {
            throw new RequestException(e.getMessage(), e);
        }
    }
}
