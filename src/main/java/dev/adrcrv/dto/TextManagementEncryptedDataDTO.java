package dev.adrcrv.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TextManagementEncryptedDataDTO {
    @NotNull
    private Long id;

    @NotNull
    private String privateKey;
    
    @NotNull
    private String privateKeyPassword;
}
