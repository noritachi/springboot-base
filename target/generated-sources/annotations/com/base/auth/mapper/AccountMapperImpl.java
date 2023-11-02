package com.base.auth.mapper;

import com.base.auth.dto.account.AccountAdminDto;
import com.base.auth.dto.account.AccountAutoCompleteDto;
import com.base.auth.dto.account.AccountDto;
import com.base.auth.dto.group.GroupDto;
import com.base.auth.form.account.CreateAccountAdminForm;
import com.base.auth.form.account.UpdateAccountAdminForm;
import com.base.auth.form.account.UpdateProfileAdminForm;
import com.base.auth.model.Account;
import com.base.auth.model.Group;
import com.base.auth.model.Permission;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T16:29:33+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.20 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public AccountDto fromAccountToDto(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDto accountDto = new AccountDto();

        accountDto.setLastLogin( account.getLastLogin() );
        accountDto.setPhone( account.getPhone() );
        if ( account.getKind() != null ) {
            accountDto.setKind( account.getKind() );
        }
        accountDto.setFullName( account.getFullName() );
        accountDto.setIsSuperAdmin( account.getIsSuperAdmin() );
        accountDto.setId( account.getId() );
        accountDto.setAvatar( account.getAvatarPath() );
        accountDto.setEmail( account.getEmail() );
        accountDto.setUsername( account.getUsername() );
        accountDto.setGroup( groupMapper.fromEntityToGroupDto( account.getGroup() ) );

        return accountDto;
    }

    @Override
    public AccountAdminDto fromEntityToAccountAdminDto(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountAdminDto accountAdminDto = new AccountAdminDto();

        accountAdminDto.setLastLogin( account.getLastLogin() );
        accountAdminDto.setKind( account.getKind() );
        accountAdminDto.setFullName( account.getFullName() );
        accountAdminDto.setAvatar( account.getAvatarPath() );
        if ( account.getCreatedDate() != null ) {
            accountAdminDto.setCreatedDate( LocalDateTime.ofInstant( account.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        accountAdminDto.setCreatedBy( account.getCreatedBy() );
        accountAdminDto.setPhone( account.getPhone() );
        accountAdminDto.setModifiedBy( account.getModifiedBy() );
        accountAdminDto.setId( account.getId() );
        accountAdminDto.setEmail( account.getEmail() );
        accountAdminDto.setUsername( account.getUsername() );
        accountAdminDto.setGroup( groupToGroupDto( account.getGroup() ) );
        accountAdminDto.setStatus( account.getStatus() );

        return accountAdminDto;
    }

    @Override
    public Account fromCreateAccountAdminFormToAdmin(CreateAccountAdminForm createAccountAdminForm) {
        if ( createAccountAdminForm == null ) {
            return null;
        }

        Account account = new Account();

        account.setPhone( createAccountAdminForm.getPhone() );
        account.setAvatarPath( createAccountAdminForm.getAvatarPath() );
        account.setFullName( createAccountAdminForm.getFullName() );
        account.setEmail( createAccountAdminForm.getEmail() );
        account.setUsername( createAccountAdminForm.getUsername() );
        account.setStatus( createAccountAdminForm.getStatus() );
        account.setKind( createAccountAdminForm.getKind() );
        account.setPassword( createAccountAdminForm.getPassword() );

        return account;
    }

    @Override
    public void mappingFormUpdateAdminToEntity(UpdateAccountAdminForm updateAccountAdminForm, Account account) {
        if ( updateAccountAdminForm == null ) {
            return;
        }

        if ( updateAccountAdminForm.getFullName() != null ) {
            account.setFullName( updateAccountAdminForm.getFullName() );
        }
        if ( updateAccountAdminForm.getPhone() != null ) {
            account.setPhone( updateAccountAdminForm.getPhone() );
        }
        if ( updateAccountAdminForm.getStatus() != null ) {
            account.setStatus( updateAccountAdminForm.getStatus() );
        }
    }

    @Override
    public void mappingFormUpdateProfileToEntity(UpdateProfileAdminForm updateProfileAdminForm, Account account) {
        if ( updateProfileAdminForm == null ) {
            return;
        }

        if ( updateProfileAdminForm.getFullName() != null ) {
            account.setFullName( updateProfileAdminForm.getFullName() );
        }
    }

    @Override
    public AccountAutoCompleteDto fromAccountToAutoCompleteDto(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountAutoCompleteDto accountAutoCompleteDto = new AccountAutoCompleteDto();

        accountAutoCompleteDto.setAvatarPath( account.getAvatarPath() );
        accountAutoCompleteDto.setFullName( account.getFullName() );
        if ( account.getId() != null ) {
            accountAutoCompleteDto.setId( account.getId() );
        }

        return accountAutoCompleteDto;
    }

    @Override
    public List<AccountAdminDto> fromEntityListToDtoList(List<Account> content) {
        if ( content == null ) {
            return null;
        }

        List<AccountAdminDto> list = new ArrayList<AccountAdminDto>( content.size() );
        for ( Account account : content ) {
            list.add( fromEntityToAccountAdminDto( account ) );
        }

        return list;
    }

    @Override
    public List<AccountAutoCompleteDto> convertAccountToAutoCompleteDto(List<Account> list) {
        if ( list == null ) {
            return null;
        }

        List<AccountAutoCompleteDto> list1 = new ArrayList<AccountAutoCompleteDto>( list.size() );
        for ( Account account : list ) {
            list1.add( fromAccountToAutoCompleteDto( account ) );
        }

        return list1;
    }

    protected GroupDto groupToGroupDto(Group group) {
        if ( group == null ) {
            return null;
        }

        GroupDto groupDto = new GroupDto();

        groupDto.setId( group.getId() );
        groupDto.setName( group.getName() );
        groupDto.setDescription( group.getDescription() );
        groupDto.setKind( group.getKind() );
        List<Permission> list = group.getPermissions();
        if ( list != null ) {
            groupDto.setPermissions( new ArrayList<Permission>( list ) );
        }

        return groupDto;
    }
}
