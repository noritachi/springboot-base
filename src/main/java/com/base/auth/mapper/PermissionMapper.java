package com.base.auth.mapper;

import com.base.auth.dto.permission.PermissionAdminDto;
import com.base.auth.dto.permission.PermissionDto;
import com.base.auth.model.Permission;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "PCode", target = "PCode")
    PermissionDto fromEntityToDto(Permission permission);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @Mapping(source = "PCode", target = "PCode")
    PermissionAdminDto fromEntityToAdminDto(Permission permission);

    @IterableMapping(elementTargetType = PermissionAdminDto.class)
    List<PermissionAdminDto> fromEntityListToAdminDtoList(List<Permission> content);

    @IterableMapping(elementTargetType = PermissionDto.class)
    List<PermissionDto> fromEntityToDtoList(List<Permission> list);

    @IterableMapping(elementTargetType = PermissionDto.class)
    List<PermissionDto> fromEntityListToDtoList(List<Permission> content);
}
