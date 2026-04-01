package com.sensedia.sample.consents.indicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConsentStatusIndicator {
    ACTIVE("Ativo"), REVOKED("Revogado"), EXPIRED("Expirado");

    private final String value;

    public static ConsentStatusIndicator getStatusFromValue(String value) {

        if (value != null && !value.isEmpty()) {
            for (ConsentStatusIndicator indicator : ConsentStatusIndicator.values()) {
                if (indicator.getValue().equalsIgnoreCase(value)) {
                    return indicator;
                }
            }
        }

        return ConsentStatusIndicator.ACTIVE;
    }
}
