package com.sensedia.sample.consents.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensedia.sample.consents.TestcontainersConfiguration;
import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentsPageDTO;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import com.sensedia.sample.consents.repository.ConsentRepository;
import mocks.constants.ConstantsMocks;
import mocks.domain.ConsentMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ConsentsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsentRepository repository;

    private final Consent consentMock = ConsentMock.entityActiveMock();

    private static final String URI = "/v1/consents/";

    private static final String PAYLOAD = "{\"cpf\": \"262.411.640-07\"}";

    @AfterEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldGetAllConsentByStatus() {
        assertDoesNotThrow(() -> {

            Consent consent = repository.save(consentMock);

            MvcResult result = mockMvc.perform(get(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("pageSize", "10")
                            .param("orderBy", "createdAt")
                            .param("direction", "ASC")
                            .param("status", "ACTIVE"))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            ConsentsPageDTO resultPage = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            ConsentResponseDTO resultObject = resultPage.consents().getFirst();

            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
            assertEquals(consent.getId(), resultObject.id());
            assertEquals(ConstantsMocks.CPF, resultObject.cpf());
            assertEquals(ConsentStatusIndicator.ACTIVE.getValue(), resultObject.status());
        });
    }

    @Test
    void shouldGetEmptyListWhenNotHaveConsentByStatus() {
        assertDoesNotThrow(() -> {

            repository.save(consentMock);

            MvcResult result = mockMvc.perform(get(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("pageSize", "10")
                            .param("orderBy", "createdAt")
                            .param("direction", "ASC")
                            .param("status", "EXPIRED"))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            ConsentsPageDTO resultPage = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            assertEquals(Collections.emptyList(), resultPage.consents());
            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        });
    }

    @Test
    void shouldGetConsentById() {
        assertDoesNotThrow(() -> {

            Consent consent = repository.save(consentMock);

            MvcResult result = mockMvc.perform(get(URI + consent.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            ConsentResponseDTO resultObject = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
            assertEquals(consent.getId(), resultObject.id());
            assertEquals(ConstantsMocks.CPF, resultObject.cpf());
        });
    }

    @Test
    void shouldGetNotFoundWhenTryToGetUnexistentConsent() {
        assertDoesNotThrow(() -> {

            MvcResult result = mockMvc.perform(get(URI + ConstantsMocks.ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andReturn();

            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        });
    }

    @Test
    void shouldCreateConsent() {
        assertDoesNotThrow(() -> {

            MvcResult result = mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(PAYLOAD))
                    .andExpect(status().isCreated())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            ConsentResponseDTO resultObject = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            Optional<Consent> consent = repository.findById(resultObject.id());

            assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
            assertNotNull(consent);
            assertEquals(ConstantsMocks.CPF, resultObject.cpf());
        });
    }

    @Test
    void shouldGetBadRequestWhenTryToCreateConsentButAlreadyExist() {
        assertDoesNotThrow(() -> {

            repository.save(consentMock);

            MvcResult result = mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(PAYLOAD))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        });
    }

    @Test
    void shouldRevokeConsent() {
        assertDoesNotThrow(() -> {

            Consent consent = repository.save(consentMock);

            MvcResult result = mockMvc.perform(delete(URI + consent.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            ConsentResponseDTO resultObject = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
            assertEquals(consent.getId(), resultObject.id());
            //Before
            assertEquals(ConsentStatusIndicator.ACTIVE, consent.getStatus());
            //After
            assertEquals(ConsentStatusIndicator.REVOKED.getValue(), resultObject.status());
        });
    }

    @Test
    void shouldGetNotFoundWhenTryToRevokeConsent() {
        assertDoesNotThrow(() -> {

            MvcResult result = mockMvc.perform(delete(URI + ConstantsMocks.ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andReturn();

            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        });
    }

    @Test
    void shouldUpdateConsent() {
        assertDoesNotThrow(() -> {

            Consent consent = repository.save(ConsentMock.entityExpiredMock());

            MvcResult result = mockMvc.perform(patch(URI + consent.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = result.getResponse().getContentAsString();

            ConsentResponseDTO resultObject = objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });

            assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
            assertEquals(consent.getId(), resultObject.id());
            //Before
            assertEquals(ConsentStatusIndicator.EXPIRED, consent.getStatus());
            //After
            assertEquals(ConsentStatusIndicator.ACTIVE.getValue(), resultObject.status());
        });
    }

    @Test
    void shouldGetNotFoundWhenTryToUpdateConsent() {
        assertDoesNotThrow(() -> {

            MvcResult result = mockMvc.perform(patch(URI + ConstantsMocks.ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andReturn();

            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        });
    }
}
