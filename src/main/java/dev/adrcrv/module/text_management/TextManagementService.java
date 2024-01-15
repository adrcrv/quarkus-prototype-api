package dev.adrcrv.module.text_management;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextManagementService {

    public String helloWorld() {
        return "{\"message\": \"Hello World\"}";
    }
}
