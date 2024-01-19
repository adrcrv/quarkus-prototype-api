package dev.adrcrv.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MismatchedInputMapperException implements ExceptionMapper<MismatchedInputException> {

    @Override
    public final Response toResponse(final MismatchedInputException exception) {
        WebApplicationResponseException payload = new WebApplicationResponseException(exception.getOriginalMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(payload).build();
    }
}
