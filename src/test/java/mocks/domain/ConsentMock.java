package mocks.domain;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mocks.constants.ConstantsMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsentMock {

    public static Consent entityActiveMock() {
        return Consent.builder()
                .id(UUID.randomUUID())
                .cpf(ConstantsMocks.CPF)
                .status(ConsentStatusIndicator.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMonths(1))
                .build();
    }

    public static Consent entityRevokedMock() {
        return Consent.builder()
                .id(UUID.randomUUID())
                .cpf(ConstantsMocks.CPF)
                .status(ConsentStatusIndicator.REVOKED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMonths(1))
                .build();
    }

    public static Consent entityExpiredMock() {
        return Consent.builder()
                .id(UUID.randomUUID())
                .cpf(ConstantsMocks.CPF)
                .status(ConsentStatusIndicator.EXPIRED)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .expiredAt(LocalDateTime.now().minusDays(1))
                .build();
    }

    public static List<Consent> toList() {
        return Collections.singletonList(entityActiveMock());
    }

    public static Page<Consent> pageEntityMock() {
        return new PageImpl<>(toList());
    }
}
