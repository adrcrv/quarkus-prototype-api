package dev.adrcrv.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dev.adrcrv.dto.TextManagementGetReqDTO;
import dev.adrcrv.dto.TextManagementGetResDTO;
import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;
import dev.adrcrv.entity.TextManagement;
import dev.adrcrv.repository.TextManagementRepository;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class TextManagementServiceTest {
    @Inject
    private TextManagementService textManagementService;

    private static Long standardId;
    private static Long encryptedIt;
    private static String privateKey;

    private static final String TEXT_DATA = "Hello World";
    private static final String PRIVATE_KEY_PASSWORD = "Vrg7$$@0ktEHrBUO79V5WbI5";
    private static final Integer KEY_SIZE = 1024;

    @BeforeAll
    public final void setupStandardData() throws Exception {
        TextManagementPostReqDTO textManagement = new TextManagementPostReqDTO();
        textManagement.setEncryption(false);
        textManagement.setTextData(TEXT_DATA);

        TextManagementPostResDTO payload = textManagementService.create(textManagement);
        standardId = payload.getId();
    }

    @BeforeAll
    public final void setupEncryptedData() throws Exception {
        TextManagementPostReqDTO textManagement = new TextManagementPostReqDTO();
        textManagement.setEncryption(true);
        textManagement.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);
        textManagement.setKeySize(KEY_SIZE);
        textManagement.setTextData(TEXT_DATA);

        TextManagementPostResDTO payload = textManagementService.create(textManagement);
        encryptedIt = payload.getId();
        privateKey = payload.getPrivateKey();
    }

    @Test
    public void expectGetByParamsThrowNotFoundException() throws Exception {
        TextManagementGetReqDTO params = new TextManagementGetReqDTO();
        params.setId(999999L);

        Assertions.assertThrows(NotFoundException.class, () -> textManagementService.getByParams(params));
    }

    @Test
    public void expectGetByParamsThrowServiceUnavailableException() throws Exception {
        Long id = 888L;

        TextManagement textManagement = new TextManagement();
        textManagement.setId(id);
        textManagement.setEncryption(false);

        TextManagementRepository textManagementRepository = mock(TextManagementRepository.class);
        when(textManagementRepository.findById(id)).thenReturn(textManagement);
        QuarkusMock.installMockForType(textManagementRepository, TextManagementRepository.class);

        TextManagementGetReqDTO params = new TextManagementGetReqDTO();
        params.setId(id);

        Assertions.assertThrows(ServiceUnavailableException.class, () -> textManagementService.getByParams(params));
    }

    @Test
    public void expectGetByParamsStandardDataAssertEquals() throws Exception {
        TextManagementGetReqDTO params = new TextManagementGetReqDTO();
        params.setId(standardId);

        TextManagementGetResDTO received = textManagementService.getByParams(params);

        TextManagementGetResDTO expected = new TextManagementGetResDTO();
        expected.setId(standardId);
        expected.setTextData(TEXT_DATA);
        expected.setEncryption(false);

        Assertions.assertEquals(received, expected);
    }

    @Test
    public void expectGetByParamsEncryptedAssertEquals() throws Exception {
        TextManagementGetReqDTO params = new TextManagementGetReqDTO();
        params.setId(encryptedIt);
        params.setPrivateKey(privateKey);
        params.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);

        TextManagementGetResDTO received = textManagementService.getByParams(params);

        TextManagementGetResDTO expected = new TextManagementGetResDTO();
        expected.setId(encryptedIt);
        expected.setEncryption(true);
        expected.setTextData(TEXT_DATA);

        Assertions.assertEquals(received, expected);
    }

    @Test
    public void expectGetByParamsEncryptedDataThrowBadRequest() throws Exception {
        TextManagementGetReqDTO params = new TextManagementGetReqDTO();
        params.setId(encryptedIt);

        Assertions.assertThrows(ConstraintViolationException.class, () -> textManagementService.getByParams(params));
    }

    @Test
    public void expectGetByParamsEncryptedDataWithInvalidPasswordThrowForbidden() throws Exception {
        TextManagementGetReqDTO params = new TextManagementGetReqDTO();
        params.setId(encryptedIt);
        params.setPrivateKey(privateKey);
        params.setPrivateKeyPassword("Wr0ng#P4ssw0rd");

        Assertions.assertThrows(ForbiddenException.class, () -> textManagementService.getByParams(params));
    }

    @Test
    public void expectGetByParamsEncryptedDataWithInvalidPrivateKeyThrowForbidden() throws Exception {
        TextManagementGetReqDTO params = new TextManagementGetReqDTO();
        params.setId(encryptedIt);
        params.setPrivateKey("Wr0ng#Priv4te@Key");
        params.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);

        Assertions.assertThrows(ForbiddenException.class, () -> textManagementService.getByParams(params));
    }

    @Test
    public void expectCreateStandardDataAssertEquals() throws Exception {
        TextManagementPostReqDTO body = new TextManagementPostReqDTO();
        body.setTextData("Hoop Hoop!");
        body.setEncryption(false);

        TextManagementPostResDTO received = textManagementService.create(body);

        TextManagementPostResDTO expected = new TextManagementPostResDTO();
        expected.setId(received.getId());

        Assertions.assertEquals(received, expected);
    }

    @Test
    public void expectCreateEncryptedDataAssertEquals() throws Exception {
        TextManagementPostReqDTO body = new TextManagementPostReqDTO();
        body.setEncryption(true);
        body.setKeySize(KEY_SIZE);
        body.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);
        body.setTextData(TEXT_DATA);

        TextManagementPostResDTO received = textManagementService.create(body);

        TextManagementPostResDTO expected = new TextManagementPostResDTO();
        expected.setId(received.getId());
        expected.setPrivateKey(received.getPrivateKey());

        Assertions.assertEquals(received, expected);
        Assertions.assertNotNull(received.getPrivateKey());
    }
}
