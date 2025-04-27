package com.glim.borad.dto.response;

import com.glim.borad.domain.Bgms;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewBgmResponse {

    private String title;
    private String artist;

    public ViewBgmResponse(Bgms bgm) {
        this.title = bgm.getTitle();
        this.artist = bgm.getArtist();
    }

}
