package dev.adrcrv.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ThrowableMapperException implements ExceptionMapper<WebApplicationException> {

    @Override
    public final Response toResponse(final WebApplicationException exception) {
        Integer statusCode = exception.getResponse().getStatus();
        ThrowableResponseException payload = new ThrowableResponseException(exception.getMessage());
        return Response.status(statusCode).entity(payload).build();
    }
}
