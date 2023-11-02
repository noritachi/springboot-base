package com.base.auth.controller;


import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.*;
import com.base.auth.dto.account.AccountAdminDto;
import com.base.auth.dto.account.AccountDto;
import com.base.auth.dto.account.ForgetPasswordDto;
import com.base.auth.dto.account.RequestForgetPasswordForm;
import com.base.auth.exception.RequestException;
import com.base.auth.exception.UnauthorizationException;
import com.base.auth.form.account.CreateAccountAdminForm;
import com.base.auth.form.account.ForgetPasswordForm;
import com.base.auth.form.account.UpdateAccountAdminForm;
import com.base.auth.form.account.UpdateProfileAdminForm;
import com.base.auth.mapper.AccountMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Group;
import com.base.auth.model.Service;
import com.base.auth.model.criteria.AccountCriteria;
import com.base.auth.repository.AccountRepository;
import com.base.auth.service.UserBaseApiService;
import com.base.auth.utils.AESUtils;
import com.base.auth.utils.ConvertUtils;
import com.base.auth.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AccountController extends ABasicController{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    UserBaseApiService userBaseApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_L')")
    public ApiMessageDto<ResponseListObj<AccountAdminDto>> list(AccountCriteria accountCriteria, Pageable pageable) {
        if(!isSuperAdmin() ){
            throw new UnauthorizationException("Not allowed to list career.");
        }
        ApiMessageDto<ResponseListObj<AccountAdminDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Account> accountPage = accountRepository.findAll(accountCriteria.getSpecification(),pageable);

        // ResponseListDto<Service> responseListDto = new ResponseListDto(careerList.getContent(), careerList.getTotalElements(), careerList.getTotalPages());

        ResponseListObj<AccountAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(accountMapper.fromEntityListToDtoList(accountPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(accountPage.getTotalPages());
        responseListObj.setTotalElements(accountPage.getTotalElements());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get career list success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create_admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_C_AD')")
    public ApiResponse<String> createAdmin(@Valid @RequestBody CreateAccountAdminForm createAccountAdminForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Account accountUsername = accountRepository.findAccountByUsername(createAccountAdminForm.getUsername());
        if (accountUsername != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_USERNAME_EXIST);
            return apiMessageDto;
        }
        Group group = groupRepository.findById(createAccountAdminForm.getGroupId()).orElse(null);
        if (group == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_UNKNOWN);
            return apiMessageDto;
        }

        Account account = accountMapper.fromCreateAccountAdminFormToAdmin(createAccountAdminForm);
        account.setGroup(group);
        account.setPassword(passwordEncoder.encode(createAccountAdminForm.getPassword()));
        account.setKind(createAccountAdminForm.getKind());

        accountRepository.save(account);

        apiMessageDto.setMessage("Create account admin success");
        return apiMessageDto;

    }

    @PutMapping(value = "/update_admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_U_AD')")
    public ApiResponse<String> updateAdmin(@Valid @RequestBody UpdateAccountAdminForm updateAccountAdminForm, BindingResult bindingResult) {

        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Account account = accountRepository.findById(updateAccountAdminForm.getId()).orElse(null);
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Group group = groupRepository.findById(updateAccountAdminForm.getGroupId()).orElse(null);
        if (group == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_UNKNOWN);
            return apiMessageDto;
        }
        accountMapper.mappingFormUpdateAdminToEntity(updateAccountAdminForm, account);
        if (StringUtils.isNoneBlank(updateAccountAdminForm.getPassword())) {
            account.setPassword(passwordEncoder.encode(updateAccountAdminForm.getPassword()));
        }
        account.setFullName(updateAccountAdminForm.getFullName());
        if (StringUtils.isNoneBlank(updateAccountAdminForm.getAvatarPath())) {
            if(!updateAccountAdminForm.getAvatarPath().equals(account.getAvatarPath())){
                //delete old image
                userBaseApiService.deleteFile(account.getAvatarPath());
            }
            account.setAvatarPath(updateAccountAdminForm.getAvatarPath());
        }

        accountRepository.save(account);

        apiMessageDto.setMessage("Update account admin success");
        return apiMessageDto;

    }


    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_V')")
    public ApiMessageDto<AccountAdminDto> get(@PathVariable("id") Long id) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allowed to get.");
        }
        Account account = accountRepository.findById(id).orElse(null);
        ApiMessageDto<AccountAdminDto> apiMessageDto = new ApiMessageDto<>();
        if (account == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Not found account");
        }
        apiMessageDto.setData(accountMapper.fromEntityToAccountAdminDto(account));
        apiMessageDto.setMessage("Get shop profile success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allow delete");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Account not found");
        }
        userBaseApiService.deleteFile(account.getAvatarPath());
        accountRepository.deleteById(id);
        apiMessageDto.setMessage("Delete Account success");
        return apiMessageDto;
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<AccountDto> profile() {
        long id = getCurrentUser();
        Account account = accountRepository.findById(id).orElse(null);
        ApiResponse<AccountDto> apiMessageDto = new ApiResponse<>();
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(accountMapper.fromAccountToDto(account));
        apiMessageDto.setMessage("Get Account success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update_profile_admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> updateProfileAdmin(final HttpServletRequest request, @Valid @RequestBody UpdateProfileAdminForm updateProfileAdminForm, BindingResult bindingResult) {

        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        long id =getCurrentUser();
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if(!passwordEncoder.matches(updateProfileAdminForm.getOldPassword(), account.getPassword())){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_WRONG_PASSWORD);
            return apiMessageDto;
        }

        if (StringUtils.isNoneBlank(updateProfileAdminForm.getPassword())) {
            account.setPassword(passwordEncoder.encode(updateProfileAdminForm.getPassword()));
        }
        accountMapper.mappingFormUpdateProfileToEntity(updateProfileAdminForm, account);
        accountRepository.save(account);

        apiMessageDto.setMessage("Update admin account success");
        return apiMessageDto;

    }

    @PostMapping(value = "/request_forget_password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ForgetPasswordDto> requestForgetPassword(@Valid @RequestBody RequestForgetPasswordForm forgetForm, BindingResult bindingResult){
        ApiResponse<ForgetPasswordDto> apiMessageDto = new ApiResponse<>();
        Account account = accountRepository.findAccountByEmail(forgetForm.getEmail());
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        String otp = userBaseApiService.getOTPForgetPassword();
        account.setAttemptCode(0);
        account.setResetPwdCode(otp);
        account.setResetPwdTime(new Date());
        accountRepository.save(account);

        //send email
        userBaseApiService.sendEmail(account.getEmail(),"OTP: "+otp, "Reset password",false);

        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto();
        String hash = AESUtils.encrypt (account.getId()+";"+otp, true);
        forgetPasswordDto.setIdHash(hash);

        apiMessageDto.setResult(true);
        apiMessageDto.setData(forgetPasswordDto);
        apiMessageDto.setMessage("Request forget password successfull, please check email.");
        return  apiMessageDto;
    }

    @PostMapping(value = "/forget_password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Long> forgetPassword(@Valid @RequestBody ForgetPasswordForm forgetForm, BindingResult bindingResult){
        ApiResponse<Long> apiMessageDto = new ApiResponse<>();

        String[] hash = AESUtils.decrypt(forgetForm.getIdHash(),true).split(";",2);
        Long id = ConvertUtils.convertStringToLong(hash[0]);
        if(id <= 0){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_WRONG_HASH_RESET_PASS);
            return apiMessageDto;
        }


        Account account = accountRepository.findById(id).orElse(null);
        if (account == null ) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        if(account.getAttemptCode() >= UserBaseConstant.MAX_ATTEMPT_FORGET_PWD){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_LOCKED);
            return apiMessageDto;
        }

        if(!account.getResetPwdCode().equals(forgetForm.getOtp()) ||
                (new Date().getTime() - account.getResetPwdTime().getTime() >= UserBaseConstant.MAX_TIME_FORGET_PWD)){

            //tang so lan
            account.setAttemptCode(account.getAttemptCode()+1);
            accountRepository.save(account);

            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_OPT_INVALID);
            return apiMessageDto;
        }

        account.setResetPwdTime(null);
        account.setResetPwdCode(null);
        account.setAttemptCode(null);
        account.setPassword(passwordEncoder.encode(forgetForm.getNewPassword()));
        accountRepository.save(account);

        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Change password success.");
        return  apiMessageDto;
    }
}
