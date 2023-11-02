package com.base.auth.mapper;

import com.base.auth.dto.permission.PermissionAdminDto;
import com.base.auth.dto.permission.PermissionDto;
import com.base.auth.model.Permission;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T16:29:33+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.20 (Oracle Corporation)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public PermissionDto fromEntityToDto(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionDto permissionDto = new PermissionDto();

        permissionDto.setShowMenu( permission.getShowMenu() );
        permissionDto.setName( permission.getName() );
        permissionDto.setAction( permission.getAction() );
        permissionDto.setDescription( permission.getDescription() );
        permissionDto.setPCode( permission.getPCode() );
        permissionDto.setNameGroup( permission.getNameGroup() );
        permissionDto.setStatus( permission.getStatus() );
        permissionDto.setId( permission.getId() );

        return permissionDto;
    }

    @Override
    public PermissionAdminDto fromEntityToAdminDto(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionAdminDto permissionAdminDto = new PermissionAdminDto();

        permissionAdminDto.setShowMenu( permission.getShowMenu() );
        permissionAdminDto.setName( permission.getName() );
        permissionAdminDto.setAction( permission.getAction() );
        permissionAdminDto.setDescription( permission.getDescription() );
        permissionAdminDto.setPCode( permission.getPCode() );
        permissionAdminDto.setNameGroup( permission.getNameGroup() );
        permissionAdminDto.setId( permission.getId() );
        permissionAdminDto.setStatus( permission.getStatus() );
        if ( permission.getModifiedDate() != null ) {
            permissionAdminDto.setModifiedDate( LocalDateTime.ofInstant( permission.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( permission.getCreatedDate() != null ) {
            permissionAdminDto.setCreatedDate( LocalDateTime.ofInstant( permission.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        permissionAdminDto.setModifiedBy( permission.getModifiedBy() );
        permissionAdminDto.setCreatedBy( permission.getCreatedBy() );

        return permissionAdminDto;
    }

    @Override
    public List<PermissionAdminDto> fromEntityListToAdminDtoList(List<Permission> content) {
        if ( content == null ) {
            return null;
        }

        List<PermissionAdminDto> list = new ArrayList<PermissionAdminDto>( content.size() );
        for ( Permission permission : content ) {
            list.add( fromEntityToAdminDto( permission ) );
        }

        return list;
    }

    @Override
    public List<PermissionDto> fromEntityToDtoList(List<Permission> list) {
        if ( list == null ) {
            return null;
        }

        List<PermissionDto> list1 = new ArrayList<PermissionDto>( list.size() );
        for ( Permission permission : list ) {
            list1.add( fromEntityToDto( permission ) );
        }

        return list1;
    }

    @Override
    public List<PermissionDto> fromEntityListToDtoList(List<Permission> content) {
        if ( content == null ) {
            return null;
        }

        List<PermissionDto> list = new ArrayList<PermissionDto>( content.size() );
        for ( Permission permission : content ) {
            list.add( fromEntityToDto( permission ) );
        }

        return list;
    }
}
