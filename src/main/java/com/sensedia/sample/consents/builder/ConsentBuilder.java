package com.sensedia.sample.consents.builder;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentsPageDTO;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsentBuilder {

    private static final LocalDateTime EXPIRE = LocalDateTime.now().plusMonths(2L);

    public static ConsentsPageDTO from(Page<Consent> consentPage) {

        List<ConsentResponseDTO> consentDTOs = consentPage.getContent().stream().map(ConsentBuilder::from).toList();

        return ConsentsPageDTO.builder()
                .page(consentPage.getNumber())
                .pageSize(consentPage.getSize())
                .totalPages(consentPage.getTotalPages())
                .totalElements(consentPage.getNumberOfElements())
                .consents(consentDTOs)
                .build();
    }

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
                .id(UUID.randomUUID())
                .cpf(normalizeCPF)
                .createdAt(LocalDateTime.now())
                .status(ConsentStatusIndicator.ACTIVE)
                .expiredAt(EXPIRE)
                .build();
    }
}
