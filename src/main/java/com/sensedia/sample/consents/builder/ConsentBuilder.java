package com.sensedia.sample.consents.builder;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsentBuilder {

    private static final LocalDateTime EXPIRE = LocalDateTime.now().plusMonths(1L);

    public static ConsentResponseDTO from(Consent consent) {
        return ConsentResponseDTO
                .builder()
                .id(consent.getId())
                .cpf(consent.getCpf())
                .status(consent.getStatus().getValue())
                .build();
    }

    public static Consent from(String normalizeCPF) {
        return Consent
                .builder()
                .cpf(normalizeCPF)
                .status(ConsentStatusIndicator.ACTIVE)
                .expiredAt(EXPIRE)
                .build();
    }
}
