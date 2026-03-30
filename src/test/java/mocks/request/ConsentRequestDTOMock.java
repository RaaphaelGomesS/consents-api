package mocks.request;

import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mocks.constants.ConstantsMocks;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsentRequestDTOMock {

    public static ConsentRequestDTO toRequest() {
        return new ConsentRequestDTO(ConstantsMocks.CPF);
    }
}
