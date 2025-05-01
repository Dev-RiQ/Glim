package com.glim.borad.dto.response;

import com.glim.borad.domain.Bgms;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewBgmResponse {

    private Long id;
    private String title;
    private String artist;
    private String fileName;

    public ViewBgmResponse(Bgms bgm) {
        this.id = bgm.getId();
        this.title = bgm.getTitle();
        this.artist = bgm.getArtist();
        this.fileName = bgm.getFileName();
    }

}
