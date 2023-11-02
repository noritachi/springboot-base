package com.base.auth.dto.video;

import com.base.auth.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class VideoDto extends ABasicAdminDto {

    private Long id;
    private String videoTitle;
    private String videoDescription;
    private String videoUrl;
    private String videoAvatar;

}
