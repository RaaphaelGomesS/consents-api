package com.sensedia.sample.consents.service;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.exception.ConsentException;
import com.sensedia.sample.consents.repository.ConsentRepository;
import mocks.constants.ConstantsMocks;
import mocks.domain.ConsentMock;
import mocks.request.ConsentRequestDTOMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConsentServiceTest {

    @InjectMocks
    private ConsentService consentService;

    @Mock
    private ConsentRepository repository;

    private Consent consent;

    private ConsentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        consent = ConsentMock.entityActiveMock();
        requestDTO = ConsentRequestDTOMock.toRequest();
    }

    @Test
    void shouldCreateConsent() {
        assertDoesNotThrow(() -> {

            when(repository.existsByCpf(any())).thenReturn(false);
            when(repository.save(any())).thenReturn(consent);

            ConsentResponseDTO result = consentService.createConsent(requestDTO);

            assertEquals(consent.getId(), result.id());
            assertEquals(consent.getCpf(), result.cpf());
        });
    }

    @Test
    void shouldThrowExceptionWhenTryToCreateConsentButAlreadyExist() {
        Exception actualException = assertThrows(Exception.class, () -> {

            when(repository.existsByCpf(any())).thenReturn(true);

            consentService.createConsent(requestDTO);
        });

        Exception expectedException = new ConsentException("O consentimento já foi criado.", HttpStatus.BAD_REQUEST);

        assertInstanceOf(ConsentException.class, actualException);
        assertEquals(expectedException.getCause(), actualException.getCause());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    void shouldFindConsentById() {
        assertDoesNotThrow(() -> {

            when(repository.findById(any())).thenReturn(Optional.of(consent));

            Consent result = consentService.findConsentById(ConstantsMocks.ID);

            assertEquals(consent.getId(), result.getId());
            assertEquals(consent.getCpf(), result.getCpf());
        });
    }

    @Test
    void shouldThrowExceptionWhenCantFindConsent() {
        Exception actualException = assertThrows(Exception.class, () -> {

            when(repository.findById(any())).thenReturn(Optional.empty());

            consentService.findConsentById(ConstantsMocks.ID);
        });

        Exception expectedException = new ConsentException("O consentimento não foi encontrado.", HttpStatus.NOT_FOUND);

        assertInstanceOf(ConsentException.class, actualException);
        assertEquals(expectedException.getCause(), actualException.getCause());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    void shouldRevokeConsentById() {
        assertDoesNotThrow(() -> {

            when(repository.findById(any())).thenReturn(Optional.of(consent));

            consentService.revokeConsent(ConstantsMocks.ID);

            verify(repository, times(1)).save(any());
        });
    }

    @Test
    void shouldThrowExceptionWhenCantFindConsentToRevoke() {
        Exception actualException = assertThrows(Exception.class, () -> {

            when(repository.findById(any())).thenReturn(Optional.empty());

            consentService.revokeConsent(ConstantsMocks.ID);
        });

        Exception expectedException = new ConsentException("O consentimento não foi encontrado.", HttpStatus.NOT_FOUND);

        assertInstanceOf(ConsentException.class, actualException);
        assertEquals(expectedException.getCause(), actualException.getCause());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }
}
