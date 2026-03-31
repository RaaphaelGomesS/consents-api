package com.sensedia.sample.consents.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ConsentResponseDTO(UUID id, String cpf, String status) {
}
