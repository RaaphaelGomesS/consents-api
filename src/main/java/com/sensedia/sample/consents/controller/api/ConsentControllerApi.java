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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
