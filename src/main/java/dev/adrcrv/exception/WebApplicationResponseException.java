package dev.adrcrv.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebApplicationResponseException {
    private String message;
}
