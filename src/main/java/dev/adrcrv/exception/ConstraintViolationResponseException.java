package dev.adrcrv.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstraintViolationResponseException {
    private List<ConstraintViolationContentException> errors;
}
