package com.sensedia.sample.consents.controller.doc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface ConsentControllerDoc {

	@GetMapping(value = "/consents", produces = { "application/json" })
	ResponseEntity<Object> findAll();
}
