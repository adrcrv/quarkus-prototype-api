package dev.adrcrv.dto;

import org.hibernate.validator.constraints.UUID;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextManagementPostResDTO {
    @UUID
    private String id;

    @NotEmpty
    private String privateKey;
}
