package com.glim.dummy.dto.response;

import com.glim.dummy.domain.Dummy;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewDummyResponse {
    private Long id;

    public ViewDummyResponse(Dummy dummy) {
        this.id = dummy.getId();
    }
}
