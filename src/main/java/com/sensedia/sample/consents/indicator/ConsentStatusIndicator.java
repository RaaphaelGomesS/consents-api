package com.sensedia.sample.consents.indicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConsentStatusIndicator {
    ACTIVE("Ativo"), REVOKED("Revogado"), EXPIRED("Expirado");

    private final String value;
}
