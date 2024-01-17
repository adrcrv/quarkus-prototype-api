package dev.adrcrv.dto;

import org.jboss.resteasy.reactive.RestQuery;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TextManagementGetReqDTO {
    @NotNull
    @RestQuery
    private Long id;
}
