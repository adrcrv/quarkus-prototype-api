package dev.adrcrv.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TextManagementGetResDTO {
    @NotNull
    private Long Id;

    private String textData;

    private Boolean encryption;

    private Integer keySize;
    
    private String privateKeyPassword;
}
