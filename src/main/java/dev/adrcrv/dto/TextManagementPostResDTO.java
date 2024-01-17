package dev.adrcrv.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TextManagementPostResDTO {
    @NotNull
    private Long id;

    private String privateKey;    
}
