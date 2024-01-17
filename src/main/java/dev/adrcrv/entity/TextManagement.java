package dev.adrcrv.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TextManagement {
    @Id
    @GeneratedValue
    private Long id;
    
    private String textData;

    private boolean encryption;

    private Integer keySize;

    private String privateKey;

    private String publicKey;
}
