package dev.adrcrv.modules.textManagement;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TextManagementDTO extends TextManagementValidation {
    @NotEmpty
    private String textData;

    @NotNull
    private boolean encryption;

    // Custom Validation Extended
    private Integer keySize;

    // Custom Validation Extended
    private String privateKeyPassword;

    public TextManagementDTO(String textData, boolean encryption, Integer keySize, String privateKeyPassword) {
        super(encryption, keySize, privateKeyPassword);
        this.textData = textData;
        this.encryption = encryption;
        this.keySize = keySize;
        this.privateKeyPassword = privateKeyPassword;
    }
}
