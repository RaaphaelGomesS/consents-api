package com.sensedia.sample.consents.controller;

import com.sensedia.sample.consents.controller.api.ConsentControllerApi;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.service.ConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
}
