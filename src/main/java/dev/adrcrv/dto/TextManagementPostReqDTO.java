package dev.adrcrv.dto;

import org.hibernate.validator.constraints.UUID;

import dev.adrcrv.validations.TextManagementPostValidation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder()
@EqualsAndHashCode(callSuper = false)
public class TextManagementPostReqDTO extends TextManagementPostValidation {
    @UUID
    private String id;

    @NotEmpty
    private String textData;

    @NotNull
    private boolean encryption;

    // Custom Validation Extended
    private Integer keySize;

    // Custom Validation Extended
    private String privateKey;

    // Standard
    private String privateKeyPassword;

    public TextManagementPostReqDTO(String textData, boolean encryption, Integer keySize, String privateKeyPassword) {
        super(encryption, keySize, privateKeyPassword);
        this.textData = textData;
        this.encryption = encryption;
        this.keySize = keySize;
        this.privateKeyPassword = privateKeyPassword;
    }
}
