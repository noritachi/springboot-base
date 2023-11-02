package com.base.auth.dto.article;

import com.base.auth.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class ArticleAdminDto extends ABasicAdminDto {
    private Long id;
    private String title;
    private String content;
    private String avatar;
    private String banner;
    private String description;
    private Long categoryId;
    private Integer pinTop;
    private Integer kind;
}
