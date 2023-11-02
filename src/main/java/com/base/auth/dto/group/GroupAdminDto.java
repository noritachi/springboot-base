package com.base.auth.dto.group;

import com.base.auth.dto.ABasicAdminDto;
import com.base.auth.dto.permission.PermissionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GroupAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "kind")
    private int kind;
    @ApiModelProperty(name = "permissions")
    private List<PermissionDto> permissions;
}
