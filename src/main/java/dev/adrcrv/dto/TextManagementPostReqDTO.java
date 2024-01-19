package dev.adrcrv.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.adrcrv.constant.EncryptConstant;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 8, max = 128)
    private String privateKeyPassword;

    @JsonIgnore
    @AssertTrue(message = "The field privateKeyPassword is required")
    private boolean isPrivateKeyPasswordRequired() {
        if (this.encryption == null || !this.encryption) {
            return true;
        }
        return this.privateKeyPassword != null;
    }

    @JsonIgnore
    @AssertTrue(message = "The field keySize is required")
    private boolean isKeySizeRequired() {
        if (this.encryption == null || !this.encryption) {
            return true;
        }
        return this.keySize != null;
    }

    @JsonIgnore
    @AssertTrue(message = "The field keySize must matches one of the values: [1024, 2048, 4096]")
    private boolean isKeySizeOneOfValues() {
        if (this.encryption == null || !this.encryption) {
            return true;
        }
        List<Integer> matchNumbers = new ArrayList<>(Arrays.asList(EncryptConstant.KEY_SIZES));
        return matchNumbers.contains(this.keySize);
    }
}
