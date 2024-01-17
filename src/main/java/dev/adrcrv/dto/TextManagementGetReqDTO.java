package dev.adrcrv.dto;

import org.jboss.resteasy.reactive.RestQuery;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TextManagementGetReqDTO {
    @NotNull
    @RestQuery
    private Long id;

    @RestQuery
    private String privateKey;
    
    @RestQuery
    private String privateKeyPassword;

    public String getPrivateKey() {
        return privateKey != null ? privateKey.replace("\s", "+") : null;
    }
}
