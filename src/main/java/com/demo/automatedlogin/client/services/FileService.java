package com.demo.automatedlogin.client.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${requestPayloadFilesPath}")
    private String jsonTemplatesBasePath;

    public String getJsonRequestPayload(@NonNull String filename) throws IOException {
        return String.join("", Files.readAllLines(Paths.get(jsonTemplatesBasePath, filename)));
    }
}
