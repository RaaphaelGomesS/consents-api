package com.sensedia.sample.consents.controller.api;

import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = "/v1/consents")
@Tag(name = "Consentimento", description = "Endpoints para operações de consentimento para uso de dados dos usuários.")
public interface ConsentControllerApi {

    @Operation(summary = "Cria novo consentimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consentimento criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "O consentimento já foi criado.", content = @Content(schema = @Schema(hidden = true)))
    }
    )
    @PostMapping("/")
    ResponseEntity<ConsentResponseDTO> createConsent(@RequestBody @Valid ConsentRequestDTO requestDTO);

    @Operation(summary = "Busca o consentimento pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consentimento encontrado."),
            @ApiResponse(responseCode = "404", description = "O consentimento não foi encontrado.", content = @Content(schema = @Schema(hidden = true)))
    }
    )
    @GetMapping("/{id}")
    ResponseEntity<ConsentResponseDTO> getConsentById(@PathVariable UUID id);

    @Operation(summary = "Revoga o consentimento pelo id.", description = "Atualiza o status do consetimento para REVOKED caso não esteja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consentimento revogado com sucesso."),
            @ApiResponse(responseCode = "404", description = "O consentimento não foi encontrado.", content = @Content(schema = @Schema(hidden = true)))
    }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<ConsentResponseDTO> revokeConsentById(@PathVariable UUID id);

    @Operation(summary = "Atualiza o consentimento pelo id.", description = "Atualiza o status do consetimento para ACTIVE caso não esteja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consentimento atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "O consentimento não foi encontrado.", content = @Content(schema = @Schema(hidden = true)))
    }
    )
    @PatchMapping("/{id}")
    ResponseEntity<ConsentResponseDTO> updateConsent(@PathVariable UUID id);
}
