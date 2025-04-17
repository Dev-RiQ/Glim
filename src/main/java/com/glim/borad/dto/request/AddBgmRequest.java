package com.glim.borad.dto.request;

import com.glim.borad.domain.Bgms;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBgmRequest {

    private String artist;
    private String title;

    public Bgms toEntity(AddBgmRequest request) {
        return Bgms.builder()
                .artist(request.getArtist())
                .title(request.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
