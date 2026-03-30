package com.sensedia.sample.consents.controller;

import com.sensedia.sample.consents.controller.doc.ConsentControllerDoc;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ConsentController implements ConsentControllerDoc {

	@Override
	public ResponseEntity<Object> findAll() {
		log.info("Requisição recebida!!!!!!");
		return ResponseEntity.badRequest().body("Dummy");
	}
}
