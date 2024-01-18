package dev.adrcrv.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import dev.adrcrv.dto.TextManagementGetReqDTO;
import dev.adrcrv.dto.TextManagementGetResDTO;
import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;
import dev.adrcrv.service.ITextManagementService;
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
    private ITextManagementService textManagementService;

    @Inject
    private Logger log;

    @Operation(summary = "Create Text Data")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully created"),
        @APIResponse(responseCode = "400", description = "Bad Request"),
        @APIResponse(responseCode = "500", description = "Unexpected Error")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public final Response create(@Valid final TextManagementPostReqDTO body) throws Exception {
        log.info("[TextManagement Creation] HTTPS POST Request Received with Ecrypted " + body.getEncryption());
        TextManagementPostResDTO payload = textManagementService.create(body);
        log.info("[TextManagement Creation] HTTPS POST Response Sending ID " + payload.getId());
        return Response.ok(payload).build();
    }

    @Operation(summary = "Retrieve Text Data")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successfully Retrieved"),
        @APIResponse(responseCode = "400", description = "Bad Request"),
        @APIResponse(responseCode = "403", description = "Invalid Private Key or Password"),
        @APIResponse(responseCode = "503", description = "Data can't be retrieved"),
        @APIResponse(responseCode = "500", description = "Unexpected Error")
    })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public final Response getByParams(@Valid @BeanParam final TextManagementGetReqDTO params) throws Exception {
        log.info("[TextManagement Query] HTTPS GET Request Received with ID " + params.getId());
        TextManagementGetResDTO payload = textManagementService.getByParams(params);
        log.info("[TextManagement Query] HTTPS GET Response Sending ID " + params.getId());
        return Response.ok(payload).build();
    }
}
