package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListObj;
import com.base.auth.dto.video.VideoAdminDto;
import com.base.auth.exception.RequestException;
import com.base.auth.form.video.CreateVideoForm;
import com.base.auth.form.video.UpdateVideoForm;
import com.base.auth.mapper.VideoMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Video;
import com.base.auth.model.criteria.VideoCriteria;
import com.base.auth.repository.AccountRepository;
import com.base.auth.repository.VideoRepository;
import com.base.auth.service.LandingIsApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/video")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class VideoController extends ABasicController{

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    LandingIsApiService landingIsApiService;

    @Autowired
    AccountRepository accountRepository;


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<VideoAdminDto>> list(VideoCriteria videoCriteria, Pageable pageable) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.VIDEO_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<VideoAdminDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Video> list = videoRepository.findAll(videoCriteria.getSpecification(), pageable);
        ResponseListObj<VideoAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(videoMapper.fromEntityListToVideoDtoListNoVideoContent(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<VideoAdminDto> get(@PathVariable("id") Long id){
        Account currentUser = accountRepository.findById(getCurrentUser()) .orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(UserBaseConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(UserBaseConstant.USER_KIND_EMPLOYEE)
                && !currentUser.getKind().equals(UserBaseConstant.USER_KIND_COLLABORATOR)) {
            throw new RequestException(ErrorCode.VIDEO_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<VideoAdminDto> result = new ApiMessageDto<>();
        Video video = videoRepository.findById(id).orElse(null);
        if(video == null){
            throw new RequestException(ErrorCode.VIDEO_ERROR_NOT_FOUND);
        }
        if(!currentUser.getKind().equals(UserBaseConstant.USER_KIND_ADMIN)
                && !video.getStatus().equals(UserBaseConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.VIDEO_ERROR_NOT_FOUND);
        }

        result.setData(videoMapper.fromEntityToVideoDto(video));
        result.setMessage("Get video success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateVideoForm createVideoForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.VIDEO_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Video video = videoMapper.fromCreateVideoFormToEntity(createVideoForm);

        videoRepository.save(video);
        apiMessageDto.setMessage("Create video success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateVideoForm updateVideoForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.VIDEO_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Video video = videoRepository.findById(updateVideoForm.getId()).orElse(null);
        if (video == null) {
            throw new RequestException(ErrorCode.VIDEO_ERROR_NOT_FOUND);
        }

        videoMapper.fromUpdateVideoFormToEntity(updateVideoForm, video);

        if (StringUtils.isNoneBlank(updateVideoForm.getAvatar())) {
            if(!updateVideoForm.getAvatar().equals(video.getAvatar())){
                //delete old image
                landingIsApiService.deleteFile(video.getAvatar());
            }
            video.setAvatar(updateVideoForm.getAvatar());
        }

        videoRepository.save(video);

        apiMessageDto.setMessage("Update video success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.VIDEO_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();
        Video video = videoRepository.findById(id).orElse(null);
        if(video == null){
            throw new RequestException(ErrorCode.VIDEO_ERROR_NOT_FOUND);
        }
        landingIsApiService.deleteFile(video.getAvatar());
        videoRepository.delete(video);
        result.setMessage("Delete success");
        return result;
    }

}