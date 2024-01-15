package dev.adrcrv.module.text_management;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class TextManagementController {
    @Inject
    TextManagementService textManagementService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        return textManagementService.helloWorld();
    }
}
