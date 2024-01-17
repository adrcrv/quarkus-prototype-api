package dev.adrcrv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TextManagement {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    
    @Column
    private String textData;

    @Column
    private Boolean encryption;

    @Column
    private Integer keySize;

    @Column(length = 1024)
    private String privateKey;

    @Column(length = 1024)
    private String publicKey;
}
