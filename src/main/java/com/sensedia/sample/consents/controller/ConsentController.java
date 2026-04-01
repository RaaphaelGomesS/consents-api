package com.sensedia.sample.consents.controller;

import com.sensedia.sample.consents.controller.api.ConsentControllerApi;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentsPageDTO;
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
    public ResponseEntity<ConsentsPageDTO> getAllConsentsByStatus(int page, int pageSize, String orderBy, String direction, String status) {

        ConsentsPageDTO pagedResponse = service.findAllConsentsByStatus(page, pageSize, orderBy, direction, status);

        return ResponseEntity.ok(pagedResponse);
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> getConsentById(UUID id) {

        ConsentResponseDTO responseDTO = service.findConsentById(id);

        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> createConsent(ConsentRequestDTO requestDTO) {

        ConsentResponseDTO responseDTO = service.createConsent(requestDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> revokeConsentById(UUID id) {

        ConsentResponseDTO responseDTO = service.revokeConsent(id);

        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> updateConsent(UUID id) {

        ConsentResponseDTO responseDTO = service.reactivateConsent(id);

        return ResponseEntity.ok(responseDTO);
    }
}
