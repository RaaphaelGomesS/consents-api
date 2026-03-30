package com.sensedia.sample.consents.repository;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsentRepository extends MongoRepository<Consent, UUID> {

    boolean existsByCpf(String cpf);

    Page<Consent> findAllByStatus(Pageable pageable, ConsentStatusIndicator status);
}
