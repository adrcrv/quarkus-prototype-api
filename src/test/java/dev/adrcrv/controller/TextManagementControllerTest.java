package dev.adrcrv.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.adrcrv.dto.TextManagementPostReqDTO;
import dev.adrcrv.dto.TextManagementPostResDTO;
import dev.adrcrv.service.TextManagementService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class TextManagementControllerTest {
    @Inject
    private TextManagementService textManagementService;

    private static Long STANDARD_ID;
    private static Long ENCRYPTED_ID;
    private static String PRIVATE_KEY;

    private static final String TEXT_DATA = "Hello World";
    private static final String PRIVATE_KEY_PASSWORD = "Vrg7$$@0ktEHrBUO79V5WbI5";
    private static final Integer KEY_SIZE = 1024;

    @BeforeAll
    public void setupStandardData() throws Exception {
        TextManagementPostReqDTO textManagement = new TextManagementPostReqDTO();
        textManagement.setEncryption(false);
        textManagement.setTextData(TEXT_DATA);

        TextManagementPostResDTO payload = textManagementService.create(textManagement);
        STANDARD_ID = payload.getId();
    }

    @BeforeAll
    public void setupEncryptedData() throws Exception {
        TextManagementPostReqDTO textManagement = new TextManagementPostReqDTO();
        textManagement.setEncryption(true);
        textManagement.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);
        textManagement.setKeySize(KEY_SIZE);
        textManagement.setTextData(TEXT_DATA);

        TextManagementPostResDTO payload = textManagementService.create(textManagement);
        ENCRYPTED_ID = payload.getId();
        PRIVATE_KEY = payload.getPrivateKey();
    }

    @Test
    public void expectGetByParamsStandardDataReturnsOkWithPayload() throws JsonProcessingException {
        given()
            .queryParam("id", STANDARD_ID)
            .when().get("/api/v1/text-management")
            .then()
            .statusCode(200)
            .body("id", equalTo(STANDARD_ID.intValue()))
            .body("textData", equalTo(TEXT_DATA))
            .body("encryption", equalTo(false));
    }

    @Test
    public void expectGetByParamsEncryptedDataReturnsOkWithPayload() throws JsonProcessingException {
        given()
            .queryParam("id", ENCRYPTED_ID)
            .queryParam("privateKey", PRIVATE_KEY)
            .queryParam("privateKeyPassword", PRIVATE_KEY_PASSWORD)
            .when().get("/api/v1/text-management")
            .then()
            .statusCode(200)
            .body("id", equalTo(ENCRYPTED_ID.intValue()))
            .body("textData", equalTo(TEXT_DATA))
            .body("encryption", equalTo(true));
    }

    @Test
    public void expectGetByParamsReturnsNotFoundWithPayload() throws JsonProcessingException {
        given()
            .queryParam("id", 999999)
            .when().get("/api/v1/text-management")
            .then()
            .statusCode(404)
            .body(is("{\"message\":\"HTTP 404 Not Found\"}"));
    }

    @Test
    public void expectGetByParamsReturnsBadRequestWithPayload() throws JsonProcessingException {
        given()
            .when().get("/api/v1/text-management")
            .then()
            .statusCode(400)
            .body(is("{\"errors\":[{\"reference\":\"getByParams.params.id\",\"message\":\"must not be null\"}]}"));
    }

    @Test
    public void expectGetByParamsEncryptedDataWithInvalidPrivateKeyReturnsForbiddenWithPayload() throws JsonProcessingException {
        given()
            .queryParam("id", ENCRYPTED_ID)
            .queryParam("privateKey", "Wr8ng#Pr1v4t3#K3y")
            .queryParam("privateKeyPassword", PRIVATE_KEY_PASSWORD)
            .when().get("/api/v1/text-management")
            .then()
            .statusCode(403)
            .body(is("{\"message\":\"HTTP 403 Forbidden\"}"));
    }

    @Test
    public void expectGetByParamsEncryptedDataWithInvalidPasswordReturnsForbiddenWithPayload() throws JsonProcessingException {
        given()
            .queryParam("id", ENCRYPTED_ID)
            .queryParam("privateKey", PRIVATE_KEY)
            .queryParam("privateKeyPassword", "Wr0ng#P4ssw0rd")
            .when().get("/api/v1/text-management")
            .then()
            .statusCode(403)
            .body(is("{\"message\":\"HTTP 403 Forbidden\"}"));
    }

    @Test
    public void expectCreateStandardDataReturnsOkWithPayload() throws JsonProcessingException {
        given()
            .contentType(ContentType.JSON)
            .body("{\"textData\": \""+TEXT_DATA+"\", \"encryption\": false}")
            .when().post("/api/v1/text-management")
            .then()
            .statusCode(200)
            .body("id", isA(Integer.class));
    }

    @Test
    public void expectCreateEncryptedDataReturnsOkWithPayload() throws JsonProcessingException {
        given()
            .contentType(ContentType.JSON)
            .body("{\"textData\": \""+TEXT_DATA+"\", \"encryption\": true, \"privateKeyPassword\": \""+PRIVATE_KEY_PASSWORD+"\", \"keySize\": "+KEY_SIZE+"}")
            .when().post("/api/v1/text-management")
            .then()
            .statusCode(200)
            .body("id", isA(Integer.class))
            .body("privateKey", isA(String.class))
            .body("privateKey", notNullValue());
    }
}
