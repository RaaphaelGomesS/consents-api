package com.sensedia.sample.consents.service;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import com.sensedia.sample.consents.repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsentExpirationWorker {

    private final ConsentRepository repository;

    @Scheduled(cron = "0 0 12 * * * ")
    public void checkExpirationDates() {

        log.info("Executando verificação de consentimentos expirados: ");

        LocalDateTime now = LocalDateTime.now();

        List<Consent> expiredConsents = repository.findAllByStatusAndExpiredAtBefore(ConsentStatusIndicator.ACTIVE, now);

        log.info("Consentimentos expirados: {}", expiredConsents);

        if (!expiredConsents.isEmpty()) {
            expiredConsents.forEach(consent -> consent.setStatus(ConsentStatusIndicator.EXPIRED));
        }

        repository.saveAll(expiredConsents);
    }
}
