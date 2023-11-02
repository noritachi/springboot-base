package com.base.auth.controller;

import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListObj;
import com.base.auth.dto.group.GroupAdminDto;
import com.base.auth.exception.RequestException;
import com.base.auth.exception.UnauthorizationException;
import com.base.auth.form.group.CreateGroupForm;
import com.base.auth.form.group.UpdateGroupForm;
import com.base.auth.mapper.GroupMapper;
import com.base.auth.model.Group;
import com.base.auth.model.Permission;
import com.base.auth.model.criteria.GroupCriteria;
import com.base.auth.repository.GroupRepository;
import com.base.auth.repository.PermissionRepository;
import com.base.auth.dto.ResponseListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class    GroupController extends ABasicController{


    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    GroupMapper groupMapper;

    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('GR_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateGroupForm createGroupForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Group group = groupRepository.findFirstByName(createGroupForm.getName());
        if(group != null){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Group name is exist");
            return apiMessageDto;
        }
        group = new Group();
        group.setName(createGroupForm.getName());
        group.setDescription(createGroupForm.getDescription());
        group.setKind(createGroupForm.getKind());
        List<Permission> permissions = new ArrayList<>();
        for(int i=0;i< createGroupForm.getPermissions().length;i++){
            Permission permission = permissionRepository.findById(createGroupForm.getPermissions()[i]).orElse(null);
            if(permission != null){
                permissions.add(permission);
            }
        }
        group.setStatus(1);
        group.setPermissions(permissions);
        groupRepository.save(group);
        apiMessageDto.setMessage("Create group success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('GR_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateGroupForm updateGroupForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Group group = groupRepository.findById(updateGroupForm.getId()).orElse(null);
        if(group == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Group name doesnt exist");
            return apiMessageDto;
        }
        //check su ton tai cua group name khac khi dat ten.
        Group otherGroup = groupRepository.findFirstByName(updateGroupForm.getName());
        if(otherGroup != null && !Objects.equals( updateGroupForm.getId(),otherGroup.getId()) ){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Cant update this group name because it is exist");
            return apiMessageDto;
        }
        group.setName(updateGroupForm.getName());
        group.setDescription(updateGroupForm.getDescription());
        List<Permission> permissions = new ArrayList<>();
        for(int i=0;i< updateGroupForm.getPermissions().length;i++){
            Permission permission = permissionRepository.findById(updateGroupForm.getPermissions()[i]).orElse(null);
            if(permission != null){
                permissions.add(permission);
            }
        }
        group.setPermissions(permissions);
        groupRepository.save(group);
        apiMessageDto.setMessage("Update group success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('GR_V')")
    public ApiMessageDto<GroupAdminDto> get(@PathVariable("id")  Long id) {
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed to get.");
        }
        ApiMessageDto<GroupAdminDto> apiMessageDto = new ApiMessageDto<>();
        Group group =groupRepository.findById(id).orElse(null);
        apiMessageDto.setData(groupMapper.fromEntityToGroupAdminDto(group));
        apiMessageDto.setMessage("Get group success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('GR_L')")
    public ApiMessageDto<ResponseListObj<GroupAdminDto>> getList(GroupCriteria groupCriteria){
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.GROUP_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<ResponseListObj<GroupAdminDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Group> groupPage = groupRepository.findAll(groupCriteria.getSpecification(),Pageable.unpaged());

        ResponseListObj<GroupAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(groupMapper.fromEntityListToAdminDtoList(groupPage.getContent()));

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List group success");
        return apiMessageDto;
    }

}
