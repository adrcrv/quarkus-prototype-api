package dev.adrcrv.modules.textManagement;

import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/text-management")
public class TextManagementController {
    @Inject
    private TextManagementService textManagementService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(@Valid TextManagementPostReqDTO body) {
        TextManagementPostResDTO payload = TextManagementPostResDTO.builder().id("123").build();
        return Response.ok(payload).build();
    }
}
