package dev.adrcrv.service;

import java.util.Set;

import dev.adrcrv.dto.KeyPairDTO;
import dev.adrcrv.dto.TextManagementGetResDTO;
import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;
import dev.adrcrv.entity.TextManagement;
import dev.adrcrv.repository.TextManagementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;

@ApplicationScoped
public class TextManagementService {
    @Inject
    private Validator validator;

    @Inject
    private TextManagementRepository textManagementRepository;

    @Inject
    private EncryptionService encryptionService;

    public TextManagementGetResDTO getById(Long id) {
        TextManagement data = textManagementRepository.findById(id);

        if (data == null) {
            throw new NotFoundException();
        }

        TextManagementGetResDTO payload = getPayloadBuilder(data);

        Set<ConstraintViolation<TextManagementGetResDTO>> violations = isGetDataValid(payload);
        
        if (!violations.isEmpty()) {
            throw new ServiceUnavailableException();
        }

        return payload;
    }

    private TextManagementGetResDTO getPayloadBuilder(TextManagement data) {
        TextManagementGetResDTO payload = new TextManagementGetResDTO();
        payload.setId(data.getId());
        payload.setTextData(data.getTextData());
        payload.setEncryption(data.getEncryption());
        payload.setKeySize(data.getKeySize());
        return payload;
    }

    @Transactional
    public TextManagementPostResDTO create(TextManagementPostReqDTO body) throws Exception {
        if (!body.getEncryption()) {
            TextManagement data = createStandardData(body);
            TextManagementPostResDTO payload = creationStandardPayloadBuilder(data);
            return payload;
        }

        Integer keySize = body.getKeySize();
        KeyPairDTO keyPair = encryptionService.generateKeyPair(keySize);
        String privateKey = keyPair.getPrivateKey();

        TextManagement data = createEncryptedData(body, keyPair);
        TextManagementPostResDTO payload = creationEncryptedPayloadBuilder(data, privateKey);

        return payload;
    }

    private TextManagement createStandardData(TextManagementPostReqDTO body) {
        TextManagement data = new TextManagement();
        data.setTextData(body.getTextData());
        data.setEncryption(body.getEncryption());

        textManagementRepository.persistAndFlush(data);

        return data;
    }

    private TextManagementPostResDTO creationStandardPayloadBuilder(TextManagement data) {
        TextManagementPostResDTO payload = new TextManagementPostResDTO();
        payload.setId(data.getId());
        return payload;
    }

    private TextManagement createEncryptedData(TextManagementPostReqDTO body, KeyPairDTO keyPair) throws Exception {
        Integer keySize = body.getKeySize();
        String textData = body.getTextData();
        Boolean encryption = body.getEncryption();
        String privateKeyPassword = body.getPrivateKeyPassword();

        String privateKey = keyPair.getPrivateKey();
        String publicKey = keyPair.getPublicKey();

        String privateKeyEncrypted = encryptionService.encryptKey(privateKey, privateKeyPassword, keySize);
        String dataEncrypted = encryptionService.encrypt(textData, publicKey);

        TextManagement data = new TextManagement();
        data.setKeySize(keySize);
        data.setTextData(dataEncrypted);
        data.setEncryption(encryption);
        data.setPrivateKey(privateKeyEncrypted);
        data.setPublicKey(publicKey);

        textManagementRepository.persistAndFlush(data);

        return data;
    }

    private TextManagementPostResDTO creationEncryptedPayloadBuilder(TextManagement data, String privateKey) {
        TextManagementPostResDTO payload = new TextManagementPostResDTO();
        payload.setId(data.getId());
        payload.setPrivateKey(privateKey);
        return payload;
    }

    private Set<ConstraintViolation<TextManagementGetResDTO>> isGetDataValid(TextManagementGetResDTO data) {
        return validator.validate(data);
    }
}
