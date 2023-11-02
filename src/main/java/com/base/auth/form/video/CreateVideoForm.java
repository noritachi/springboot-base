package com.base.auth.form.video;

import com.base.auth.validation.CategoryKind;
import com.base.auth.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateVideoForm {

    @NotEmpty(message = "title cannot be null")
    @ApiModelProperty(required = true)
    private String title;

    @NotEmpty(message = "url cannot be null")
    @ApiModelProperty(required = true)
    private String url;

    @NotEmpty(message = "avatar cannot be null")
    @ApiModelProperty(required = true)
    private String avatar;

    @NotEmpty(message = "description cannot be null")
    @ApiModelProperty(required = true)
    private String description;

    @NotNull(message = "kind cannot be null")
    @ApiModelProperty(required = true)
    @CategoryKind
    private Integer kind;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    @Status
    private Integer status;
}