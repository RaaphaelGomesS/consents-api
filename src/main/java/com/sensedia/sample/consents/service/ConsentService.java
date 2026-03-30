package com.sensedia.sample.consents.service;

import com.sensedia.sample.consents.builder.ConsentBuilder;
import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentsPageDTO;
import com.sensedia.sample.consents.exception.ConsentException;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import com.sensedia.sample.consents.repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository repository;

    public ConsentsPageDTO findAllConsentsByStatus(int page, int pageSize, String orderBy, String direction, ConsentStatusIndicator status) {

        Sort sort = Sort.by(Sort.Direction.valueOf(direction), orderBy);

        PageRequest pageable = PageRequest.of(page, pageSize, sort);

        Page<Consent> consentPage = repository.findAllByStatus(pageable, status);

        return ConsentBuilder.from(consentPage);
    }

    public ConsentResponseDTO createConsent(ConsentRequestDTO requestDTO) {

        String normalizeCPF = normalizeCPF(requestDTO.cpf());

        log.info("CPF normalizado: {}", normalizeCPF);

        if (repository.existsByCpf(normalizeCPF)) {
            throw new ConsentException("O consentimento já foi criado.", HttpStatus.BAD_REQUEST);
        }

        Consent consent = ConsentBuilder.from(normalizeCPF);

        Consent savedConsent = repository.save(consent);

        return ConsentBuilder.from(savedConsent);
    }

    public ConsentResponseDTO reactivateConsent(UUID id) {
        Consent consent = findConsentById(id);

        if (consent.getStatus() != ConsentStatusIndicator.ACTIVE) {
            consent.setStatus(ConsentStatusIndicator.ACTIVE);
            consent.setExpiredAt(LocalDateTime.now().plusMonths(1L));

            consent = repository.save(consent);
        }

        return ConsentBuilder.from(consent);
    }

    public ConsentResponseDTO revokeConsent(UUID id) {
        Consent consent = findConsentById(id);

        if (consent.getStatus() != ConsentStatusIndicator.REVOKED) {
            consent.setStatus(ConsentStatusIndicator.REVOKED);
            consent.setExpiredAt(null);

            consent = repository.save(consent);
        }

        return ConsentBuilder.from(consent);
    }

    public Consent findConsentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ConsentException("O consentimento não foi encontrado.", HttpStatus.NOT_FOUND));
    }

    private String normalizeCPF(String cpf) {
        return cpf.replace("\\D", "");
    }
}
