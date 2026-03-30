package com.sensedia.sample.consents.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ConsentsPageDTO(int page,
                              int pageSize,
                              int totalPages,
                              int totalElements,
                              List<ConsentResponseDTO> consents) {
}
