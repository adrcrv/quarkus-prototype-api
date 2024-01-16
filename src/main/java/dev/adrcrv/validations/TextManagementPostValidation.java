package dev.adrcrv.validations;

import java.util.ArrayList;
import java.util.Arrays;

import dev.adrcrv.constants.EncryptConstant;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder()
@AllArgsConstructor
public class TextManagementPostValidation {
    private boolean encryption;
    private Integer keySize;
    private String privateKeyPassword;

    @AssertTrue(message = "The field privateKeyPassword is required")
    private boolean isPrivateKeyPasswordRequired() {
        System.out.println(this.encryption);
        boolean hasEncryption = this.encryption == true;
        boolean hasPrivateKeyPassword = this.privateKeyPassword != null;
        boolean hasEncryptionAndPrivateKeyPassword = hasEncryption && hasPrivateKeyPassword;
        return !hasEncryption || hasEncryptionAndPrivateKeyPassword;
    }

    @AssertTrue(message = "The field keySize is required")
    private boolean isKeySizeRequired() {
        boolean hasEncryption = this.encryption == true;
        boolean hasKeySize = this.keySize != null;
        boolean hasEncryptionAndKeySize = hasEncryption && hasKeySize;
        return !hasEncryption || hasEncryptionAndKeySize;
    }

    @AssertTrue(message = "The field keySize must matches one of the values: [1024, 2048, 4096]")
    private boolean isKeySizeOneOfValues() {
        if (!this.encryption) return true;
        ArrayList<Integer> matchNumbers = new ArrayList<>(Arrays.asList(EncryptConstant.KEY_SIZES));
        return matchNumbers.contains(this.keySize);
    }
}
