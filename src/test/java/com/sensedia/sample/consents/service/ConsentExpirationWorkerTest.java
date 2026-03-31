package com.sensedia.sample.consents.service;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.repository.ConsentRepository;
import mocks.domain.ConsentMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConsentExpirationWorkerTest {

    @InjectMocks
    private ConsentExpirationWorker consentExpirationWorker;

    @Mock
    private ConsentRepository repository;

    private List<Consent> consents;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        consents = ConsentMock.toList();
    }

    @Test
    void shouldCheckExpirationDates() {
        assertDoesNotThrow(() -> {

            when(repository.findAllByStatusAndExpiredAtBefore(any(), any())).thenReturn(Collections.emptyList());

            consentExpirationWorker.checkExpirationDates();

            verify(repository, times(0)).saveAll(any());
        });
    }

    @Test
    void shouldCheckExpirationDatesAndUpdateStatusWhenFindExpiredDates() {
        assertDoesNotThrow(() -> {

            when(repository.findAllByStatusAndExpiredAtBefore(any(), any())).thenReturn(consents);

            consentExpirationWorker.checkExpirationDates();

            verify(repository, times(1)).saveAll(any());
        });
    }
}
