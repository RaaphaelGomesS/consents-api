package com.sensedia.sample.consents.domain;

import com.sensedia.sample.consents.indicator.ConsentStatusIndicator;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document(collection = "consents")
public class Consent {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String cpf;

    private ConsentStatusIndicator status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;
}
