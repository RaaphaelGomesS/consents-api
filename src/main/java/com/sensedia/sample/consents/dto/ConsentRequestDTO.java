package com.sensedia.sample.consents.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsentRequestDTO(
        @NotBlank(message = "O campo não pode estar em branco.")
        @CPF(message = "O CPF deve ser válido.")
        @Schema(description = "CPF do usuário", example = "123.456.789-12")
        String cpf) {
}
