package com.base.auth.mapper;

import com.base.auth.dto.account.AccountAdminDto;
import com.base.auth.dto.account.AccountAutoCompleteDto;
import com.base.auth.dto.account.AccountDto;
import com.base.auth.form.account.CreateAccountAdminForm;
import com.base.auth.form.account.UpdateAccountAdminForm;
import com.base.auth.form.account.UpdateProfileAdminForm;
import com.base.auth.model.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class})
public interface AccountMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromAccountToDto")
    AccountDto fromAccountToDto(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    AccountAdminDto fromEntityToAccountAdminDto(Account account);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    Account fromCreateAccountAdminFormToAdmin(CreateAccountAdminForm createAccountAdminForm);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "phone", target = "phone")
    @BeanMapping(ignoreByDefault = true)
    void mappingFormUpdateAdminToEntity(UpdateAccountAdminForm updateAccountAdminForm, @MappingTarget Account account);

    @Mapping(source = "fullName", target = "fullName")
    @BeanMapping(ignoreByDefault = true)
    void mappingFormUpdateProfileToEntity(UpdateProfileAdminForm updateProfileAdminForm, @MappingTarget Account account);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "fullName", target = "fullName")
    AccountAutoCompleteDto fromAccountToAutoCompleteDto(Account account);

    @IterableMapping(elementTargetType = AccountAdminDto.class, qualifiedByName = "adminGetMapping")
    List<AccountAdminDto> fromEntityListToDtoList(List<Account> content);

    @IterableMapping(elementTargetType = AccountAutoCompleteDto.class)
    List<AccountAutoCompleteDto> convertAccountToAutoCompleteDto(List<Account> list);
}
