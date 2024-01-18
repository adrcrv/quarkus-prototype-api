package dev.adrcrv.controller;

import dev.adrcrv.dto.TextManagementGetReqDTO;
import dev.adrcrv.dto.TextManagementGetResDTO;
import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;
import dev.adrcrv.service.TextManagementService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
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
    public final Response create(@Valid final TextManagementPostReqDTO body) throws Exception {
        TextManagementPostResDTO payload = textManagementService.create(body);
        return Response.ok(payload).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public final Response getByParams(@Valid @BeanParam final TextManagementGetReqDTO params) throws Exception {
        TextManagementGetResDTO payload = textManagementService.getByParams(params);
        return Response.ok(payload).build();
    }
}
