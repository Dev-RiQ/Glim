package com.glim.borad.dto.response;

import com.glim.borad.domain.Bgms;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewBgmResponse {

    private final Long id;
    private final String title;
    private final String artist;
    private final String fileName;

    public ViewBgmResponse(Bgms bgm,String fileName) {
        this.id = bgm.getId();
        this.title = bgm.getTitle();
        this.artist = bgm.getArtist();
        this.fileName =fileName;
    }

}
