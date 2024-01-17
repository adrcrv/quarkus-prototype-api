package dev.adrcrv.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.adrcrv.constant.EncryptConstant;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TextManagementPostReqDTO {
    @NotEmpty
    private String textData;

    @NotNull
    private Boolean encryption;

    // Custom Validation
    private Integer keySize;

    // Custom Validation
    private String privateKeyPassword;

    @AssertTrue(message = "The field privateKeyPassword is required")
    private boolean isPrivateKeyPasswordRequired() {
        if (!this.encryption) {
            return true;
        }
        return this.privateKeyPassword != null;
    }

    @AssertTrue(message = "The field keySize is required")
    private boolean isKeySizeRequired() {
        if (!this.encryption) {
            return true;
        }
        return this.keySize != null;
    }

    @AssertTrue(message = "The field keySize must matches one of the values: [1024, 2048, 4096]")
    private boolean isKeySizeOneOfValues() {
        if (!this.encryption) {
            return true;
        }
        List<Integer> matchNumbers = new ArrayList<>(Arrays.asList(EncryptConstant.KEY_SIZES));
        return matchNumbers.contains(this.keySize);
    }
}
