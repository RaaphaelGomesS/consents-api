package com.sensedia.sample.consents.service;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentsPageDTO;
import com.sensedia.sample.consents.exception.ConsentException;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import com.sensedia.sample.consents.repository.ConsentRepository;
import mocks.constants.ConstantsMocks;
import mocks.domain.ConsentMock;
import mocks.request.ConsentRequestDTOMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
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

    private Page<Consent> consentPage;

    private ConsentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        consent = ConsentMock.entityActiveMock();
        consentPage = ConsentMock.pageEntityMock();
        requestDTO = ConsentRequestDTOMock.toRequest();
    }

    @Test
    void shouldFindAllConsentsByStatus() {
        assertDoesNotThrow(() -> {

            when(repository.findAllByStatus(any(), any())).thenReturn(consentPage);

            ConsentsPageDTO result = consentService.findAllConsentsByStatus(0, 10, "createdAt", "ASC", "Ativo");

            assertEquals(consentPage.getSize(), result.consents().size());
            assertEquals(consentPage.getTotalElements(), result.totalElements());
        });
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

            ConsentResponseDTO result = consentService.findConsentById(ConstantsMocks.ID);

            assertEquals(consent.getId(), result.id());
            assertEquals(consent.getCpf(), result.cpf());
        });
    }

    @Test
    void shouldFindConsentByIdAndUpdateStatusIfHitTheExpiredDate() {
        assertDoesNotThrow(() -> {

            consent.setExpiredAt(LocalDateTime.now().minusDays(1L));

            when(repository.findById(any())).thenReturn(Optional.of(consent));
            when(repository.save(any())).thenReturn(consent);

            ConsentResponseDTO result = consentService.findConsentById(ConstantsMocks.ID);

            assertEquals(consent.getId(), result.id());
            assertEquals(consent.getCpf(), result.cpf());
            assertEquals(ConsentStatusIndicator.EXPIRED, consent.getStatus());
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
    void shouldReactivateConsentWhenIsAlreadyActive() {
        assertDoesNotThrow(() -> {

            when(repository.findById(any())).thenReturn(Optional.of(consent));

            ConsentResponseDTO result = consentService.reactivateConsent(ConstantsMocks.ID);

            verify(repository, times(0)).save(any());

            assertEquals(consent.getId(), result.id());
            assertEquals(consent.getCpf(), result.cpf());
            assertEquals(ConsentStatusIndicator.ACTIVE.getValue(), result.status());
        });
    }

    @Test
    void shouldRevokeConsentWhenIsExpired() {
        assertDoesNotThrow(() -> {

            consent = ConsentMock.entityExpiredMock();

            when(repository.findById(any())).thenReturn(Optional.of(consent));
            when(repository.save(any())).thenReturn(consent);

            ConsentResponseDTO result = consentService.reactivateConsent(ConstantsMocks.ID);

            assertEquals(consent.getId(), result.id());
            assertEquals(consent.getCpf(), result.cpf());
            assertEquals(ConsentStatusIndicator.ACTIVE.getValue(), result.status());
        });
    }

    @Test
    void shouldThrowExceptionWhenCantFindConsentToReactive() {
        Exception actualException = assertThrows(Exception.class, () -> {

            when(repository.findById(any())).thenReturn(Optional.empty());

            consentService.reactivateConsent(ConstantsMocks.ID);
        });

        Exception expectedException = new ConsentException("O consentimento não foi encontrado.", HttpStatus.NOT_FOUND);

        assertInstanceOf(ConsentException.class, actualException);
        assertEquals(expectedException.getCause(), actualException.getCause());
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    void shouldRevokeConsentWhenIsActive() {
        assertDoesNotThrow(() -> {

            when(repository.findById(any())).thenReturn(Optional.of(consent));
            when(repository.save(any())).thenReturn(consent);

            ConsentResponseDTO result = consentService.revokeConsent(ConstantsMocks.ID);

            assertEquals(consent.getId(), result.id());
            assertEquals(consent.getCpf(), result.cpf());
            assertEquals(ConsentStatusIndicator.REVOKED.getValue(), result.status());
        });
    }

    @Test
    void shouldRevokeConsentWhenAlreadyIsRevoked() {
        assertDoesNotThrow(() -> {

            consent = ConsentMock.entityRevokedMock();

            when(repository.findById(any())).thenReturn(Optional.of(consent));

            ConsentResponseDTO result = consentService.revokeConsent(ConstantsMocks.ID);

            verify(repository, times(0)).save(any());

            assertEquals(consent.getId(), result.id());
            assertEquals(consent.getCpf(), result.cpf());
            assertEquals(ConsentStatusIndicator.REVOKED.getValue(), result.status());
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
