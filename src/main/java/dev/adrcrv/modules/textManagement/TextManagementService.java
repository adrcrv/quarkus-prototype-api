package dev.adrcrv.modules.textManagement;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextManagementService {

    public String helloWorld() {
        return "{\"message\": \"Hello World\"}";
    }
}
