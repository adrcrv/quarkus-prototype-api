package dev.adrcrv.service;

import java.util.Set;

import org.jboss.logging.Logger;

import dev.adrcrv.dto.KeyPairDTO;
import dev.adrcrv.dto.TextManagementEncryptedDataDTO;
import dev.adrcrv.dto.TextManagementGetReqDTO;
import dev.adrcrv.dto.TextManagementGetResDTO;
import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;
import dev.adrcrv.entity.TextManagement;
import dev.adrcrv.repository.ITextManagementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;

@ApplicationScoped
public class TextManagementService implements ITextManagementService {
    @Inject
    private Validator validator;

    @Inject
    private ITextManagementRepository textManagementRepository;

    @Inject
    private IEncryptionService encryptionService;

    @Inject
    private Logger log;

    @Transactional
    public final TextManagementGetResDTO getByParams(final TextManagementGetReqDTO params) throws Exception {
        log.info("[TextManagement Query] Finding Data By Id");
        TextManagement data = textManagementRepository.findById(params.getId());

        if (data == null) {
            log.info("[TextManagement Query] Throwing NotFoundException: No Data Found");
            throw new NotFoundException();
        }

        TextManagementGetResDTO payload = data.getEncryption() ? getEncryptedData(params, data) : getStandardData(data);
        Set<ConstraintViolation<TextManagementGetResDTO>> payloadViolations = validator.validate(payload);

        if (!payloadViolations.isEmpty()) {
            log.info("[TextManagement Query] Throwing ServiceUnavailableException: Constraint Violations");
            throw new ServiceUnavailableException();
        }

        log.info("[TextManagement Query] Data Found");
        return payload;
    }

    private TextManagementGetResDTO getEncryptedData(final TextManagementGetReqDTO params, final TextManagement data)
    throws Exception {
        log.info("[TextManagement Query] Decrypting Text Data");
        Set<ConstraintViolation<TextManagementEncryptedDataDTO>> dataViolations = encryptedDataViolations(params, data);

        if (!dataViolations.isEmpty()) {
            log.info("[TextManagement Query] Throwing ConstraintViolationException: Constraint Violations");
            throw new ConstraintViolationException(dataViolations);
        }

        String privateKeyEncrypted = data.getPrivateKey();
        String privateKeyPassword = params.getPrivateKeyPassword();
        Integer keySize = data.getKeySize();

        String privateKeyDecrypted = encryptionService.decryptKey(privateKeyEncrypted, privateKeyPassword, keySize);

        if (privateKeyDecrypted == null || !privateKeyDecrypted.equals(params.getPrivateKey())) {
            log.info("[TextManagement Query] Throwing ForbiddenException: Invalid Private Key or Password");
            throw new ForbiddenException();
        }

        String textData = encryptionService.decrypt(data.getTextData(), params.getPrivateKey());

        TextManagementGetResDTO payload = new TextManagementGetResDTO();
        payload.setId(data.getId());
        payload.setTextData(textData);
        payload.setEncryption(data.getEncryption());

        log.info("[TextManagement Query] Text Data Successfully Decrypted");

        return payload;
    }

    private Set<ConstraintViolation<TextManagementEncryptedDataDTO>> encryptedDataViolations(
    final TextManagementGetReqDTO params, final TextManagement data) {

        TextManagementEncryptedDataDTO validations = new TextManagementEncryptedDataDTO();
        validations.setId(data.getId());
        validations.setPrivateKey(params.getPrivateKey());
        validations.setPrivateKeyPassword(params.getPrivateKeyPassword());

        Set<ConstraintViolation<TextManagementEncryptedDataDTO>> violations = validator.validate(validations);
        return violations;
    }

    private TextManagementGetResDTO getStandardData(final TextManagement data) {
        TextManagementGetResDTO payload = new TextManagementGetResDTO();
        payload.setId(data.getId());
        payload.setTextData(data.getTextData());
        payload.setEncryption(data.getEncryption());
        return payload;
    }

    @Transactional
    public final TextManagementPostResDTO create(final TextManagementPostReqDTO body) throws Exception {
        log.info("[TextManagement Creation] Creating Data");

        if (!body.getEncryption()) {
            TextManagement data = createStandardData(body);
            TextManagementPostResDTO payload = creationStandardPayloadBuilder(data);
            return payload;
        }

        KeyPairDTO keyPair = encryptionService.generateKeyPair(body.getKeySize());
        String privateKey = keyPair.getPrivateKey();

        TextManagement data = createEncryptedData(body, keyPair);
        TextManagementPostResDTO payload = creationEncryptedPayloadBuilder(data, privateKey);

        return payload;
    }

    private TextManagement createStandardData(final TextManagementPostReqDTO body) {
        log.info("[TextManagement Creation] Creating Standard Data");

        TextManagement data = new TextManagement();
        data.setTextData(body.getTextData());
        data.setEncryption(body.getEncryption());

        textManagementRepository.persistAndFlush(data);
        log.info("[TextManagement Creation] Standard Data Created");

        return data;
    }

    private TextManagementPostResDTO creationStandardPayloadBuilder(final TextManagement data) {
        TextManagementPostResDTO payload = new TextManagementPostResDTO();
        payload.setId(data.getId());
        return payload;
    }

    private TextManagement createEncryptedData(final TextManagementPostReqDTO body, final KeyPairDTO keyPair)
    throws Exception {

        log.info("[TextManagement Creation] Creating Encrypted Data");
        String privateKey = keyPair.getPrivateKey();
        String publicKey = keyPair.getPublicKey();

        String privateKeyPassword = body.getPrivateKeyPassword();
        Integer keySize = body.getKeySize();

        String privateKeyEncrypted = encryptionService.encryptKey(privateKey, privateKeyPassword, keySize);
        String dataEncrypted = encryptionService.encrypt(body.getTextData(), publicKey);

        TextManagement data = new TextManagement();
        data.setKeySize(keySize);
        data.setTextData(dataEncrypted);
        data.setEncryption(body.getEncryption());
        data.setPrivateKey(privateKeyEncrypted);
        data.setPublicKey(publicKey);

        textManagementRepository.persistAndFlush(data);
        log.info("[TextManagement Creation] Encrypted Data Created");

        return data;
    }

    private TextManagementPostResDTO creationEncryptedPayloadBuilder(
    final TextManagement data, final String privateKey) {
        TextManagementPostResDTO payload = new TextManagementPostResDTO();
        payload.setId(data.getId());
        payload.setPrivateKey(privateKey);
        return payload;
    }
}
