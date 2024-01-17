package dev.adrcrv.exception;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationMapperException implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ConstraintViolationContentException> contents = exception.getConstraintViolations().stream()
                .map(this::toResponseMapHandler)
                .collect(Collectors.toList());

        ConstraintViolationResponseException response = new ConstraintViolationResponseException(contents);
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }

    private ConstraintViolationContentException toResponseMapHandler(ConstraintViolation<?> constraintViolation) {
        String propertyPath = constraintViolation.getPropertyPath().toString();
        String message = constraintViolation.getMessage();

        ConstraintViolationContentException constraintViolationExceptionContent = new ConstraintViolationContentException();
        constraintViolationExceptionContent.setReference(propertyPath);
        constraintViolationExceptionContent.setMessage(message);

        return constraintViolationExceptionContent;
    }

}