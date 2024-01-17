package dev.adrcrv.service;

import java.security.KeyPair;
import java.util.Set;

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

    public TextManagementPostResDTO getById(Long id) {
        TextManagement data = textManagementRepository.findById(id);

        if (data == null) {
            throw new NotFoundException();
        }

        TextManagementPostResDTO payload = new TextManagementPostResDTO();
        payload.setId(data.getId());

        Set<ConstraintViolation<TextManagementPostResDTO>> violations = isGetDataValid(payload);
        
        if (!violations.isEmpty()) {
            throw new ServiceUnavailableException();
        }

        return payload;
    }

    @Transactional
    public TextManagementPostResDTO create(TextManagementPostReqDTO body) throws Exception {
        if (!body.getEncryption()) {
            TextManagement data = createStandardData(body);
            TextManagementPostResDTO payload = creationStandardPayloadBuilder(data);
            return payload;
        }
        return null;
        // Integer keySize = body.getKeySize();
        // KeyPair keyPair = encryptionService.generateKeyPair(keySize);
    }

    private TextManagement createStandardData(TextManagementPostReqDTO body) {
        TextManagement data = new TextManagement();
        data.setTextData(body.getTextData());
        data.setEncryption(body.getEncryption());

        textManagementRepository.persistAndFlush(data);

        return data;
    }

    private TextManagement createEncryptedData(TextManagementPostReqDTO body) {
        TextManagement data = new TextManagement();
        data.setTextData(body.getTextData());
        data.setEncryption(body.getEncryption());
        data.setKeySize(body.getKeySize());

        textManagementRepository.persistAndFlush(data);

        return data;
    }

    private TextManagementPostResDTO creationStandardPayloadBuilder(TextManagement data) {
        TextManagementPostResDTO payload = new TextManagementPostResDTO();
        payload.setId(data.getId());
        return payload;
    }

    private Set<ConstraintViolation<TextManagementPostResDTO>> isGetDataValid(TextManagementPostResDTO data) {
        return validator.validate(data);
    }
}
