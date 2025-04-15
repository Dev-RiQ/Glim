package com.glim.user.dto.response;

import com.glim.user.domain.Dummy;
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
