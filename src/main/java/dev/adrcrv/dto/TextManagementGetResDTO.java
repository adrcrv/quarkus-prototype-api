package dev.adrcrv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TextManagementGetResDTO {
    @NotNull
    private Long Id;

    private String textData;

    private Boolean encryption;
}
