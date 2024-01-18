package dev.adrcrv.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThrowableResponseException {
    private String message;
}
