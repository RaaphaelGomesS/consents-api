package com.sensedia.sample.consents.controller;

import com.sensedia.sample.consents.builder.ConsentBuilder;
import com.sensedia.sample.consents.controller.api.ConsentControllerApi;
import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.service.ConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConsentController implements ConsentControllerApi {

    private final ConsentService service;

    @Override
    public ResponseEntity<ConsentResponseDTO> createConsent(ConsentRequestDTO requestDTO) {

        ConsentResponseDTO responseDTO = service.createConsent(requestDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> getConsentById(UUID id) {

        Consent consent = service.findConsentById(id);

        return ResponseEntity.ok(ConsentBuilder.from(consent));
    }

    @Override
    public ResponseEntity<Void> revokeConsentById(UUID id) {

        service.findConsentById(id);

        return ResponseEntity.ok(null);
    }
}
