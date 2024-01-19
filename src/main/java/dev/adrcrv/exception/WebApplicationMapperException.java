package dev.adrcrv.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationMapperException implements ExceptionMapper<WebApplicationException> {

    @Override
    public final Response toResponse(final WebApplicationException exception) {
        Integer statusCode = exception.getResponse().getStatus();
        WebApplicationResponseException payload = new WebApplicationResponseException(exception.getMessage());
        return Response.status(statusCode).entity(payload).build();
    }
}
