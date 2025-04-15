package com.glim.dummy.dto.request;

import com.glim.dummy.domain.Dummy;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddDummyRequest {
    private Long id;

    public Dummy toEntity(AddDummyRequest addDummyRequest) {
        return Dummy.builder()
                .id(addDummyRequest.getId())
                .build();
    }
}
