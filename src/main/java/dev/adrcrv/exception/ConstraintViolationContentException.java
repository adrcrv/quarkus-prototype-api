package dev.adrcrv.exception;

import lombok.Data;

@Data
public class ConstraintViolationContentException {
    private String reference;
    private String message;
}
