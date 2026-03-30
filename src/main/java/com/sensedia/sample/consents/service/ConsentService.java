package com.sensedia.sample.consents.service;

import com.sensedia.sample.consents.builder.ConsentBuilder;
import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.exception.ConsentException;
import com.sensedia.sample.consents.repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository repository;

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

    private String normalizeCPF(String cpf) {
        return cpf.replace("\\D", "");
    }
}
