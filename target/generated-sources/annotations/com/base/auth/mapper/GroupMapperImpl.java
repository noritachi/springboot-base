package com.base.auth.mapper;

import com.base.auth.dto.group.GroupAdminDto;
import com.base.auth.dto.group.GroupDto;
import com.base.auth.dto.permission.PermissionDto;
import com.base.auth.model.Group;
import com.base.auth.model.Permission;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T16:29:34+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.20 (Oracle Corporation)"
)
@Component
public class GroupMapperImpl implements GroupMapper {

    @Override
    public GroupDto fromEntityToGroupDto(Group group) {
        if ( group == null ) {
            return null;
        }

        GroupDto groupDto = new GroupDto();

        groupDto.setName( group.getName() );
        groupDto.setDescription( group.getDescription() );
        groupDto.setId( group.getId() );
        groupDto.setKind( group.getKind() );
        List<Permission> list = group.getPermissions();
        if ( list != null ) {
            groupDto.setPermissions( new ArrayList<Permission>( list ) );
        }

        return groupDto;
    }

    @Override
    public GroupAdminDto fromEntityToGroupAdminDto(Group group) {
        if ( group == null ) {
            return null;
        }

        GroupAdminDto groupAdminDto = new GroupAdminDto();

        groupAdminDto.setName( group.getName() );
        groupAdminDto.setDescription( group.getDescription() );
        groupAdminDto.setId( group.getId() );
        groupAdminDto.setKind( group.getKind() );
        groupAdminDto.setPermissions( permissionListToPermissionDtoList( group.getPermissions() ) );
        groupAdminDto.setStatus( group.getStatus() );
        if ( group.getModifiedDate() != null ) {
            groupAdminDto.setModifiedDate( LocalDateTime.ofInstant( group.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( group.getCreatedDate() != null ) {
            groupAdminDto.setCreatedDate( LocalDateTime.ofInstant( group.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        groupAdminDto.setModifiedBy( group.getModifiedBy() );
        groupAdminDto.setCreatedBy( group.getCreatedBy() );

        return groupAdminDto;
    }

    @Override
    public List<GroupAdminDto> fromEntityListToAdminDtoList(List<Group> content) {
        if ( content == null ) {
            return null;
        }

        List<GroupAdminDto> list = new ArrayList<GroupAdminDto>( content.size() );
        for ( Group group : content ) {
            list.add( fromEntityToGroupAdminDto( group ) );
        }

        return list;
    }

    protected PermissionDto permissionToPermissionDto(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionDto permissionDto = new PermissionDto();

        permissionDto.setId( permission.getId() );
        permissionDto.setName( permission.getName() );
        permissionDto.setAction( permission.getAction() );
        permissionDto.setShowMenu( permission.getShowMenu() );
        permissionDto.setDescription( permission.getDescription() );
        permissionDto.setNameGroup( permission.getNameGroup() );
        permissionDto.setStatus( permission.getStatus() );
        permissionDto.setPCode( permission.getPCode() );

        return permissionDto;
    }

    protected List<PermissionDto> permissionListToPermissionDtoList(List<Permission> list) {
        if ( list == null ) {
            return null;
        }

        List<PermissionDto> list1 = new ArrayList<PermissionDto>( list.size() );
        for ( Permission permission : list ) {
            list1.add( permissionToPermissionDto( permission ) );
        }

        return list1;
    }
}
