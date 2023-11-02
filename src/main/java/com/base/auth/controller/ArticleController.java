package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListObj;
import com.base.auth.dto.article.ArticleAdminDto;
import com.base.auth.exception.RequestException;
import com.base.auth.form.article.CreateArticleForm;
import com.base.auth.form.article.UpdateArticleForm;
import com.base.auth.mapper.ArticleMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Article;
import com.base.auth.model.criteria.ArticleCriteria;
import com.base.auth.repository.AccountRepository;
import com.base.auth.repository.ArticleRepository;
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
@RequestMapping("/v1/article")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ArticleController extends ABasicController{

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    LandingIsApiService landingIsApiService;

    @Autowired
    AccountRepository accountRepository;


//    @GetMapping(value = "/get_article/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiMessageDto<ArticleDto> listArticle(@PathVariable("id") Long id){
//        ApiMessageDto<ArticleDto> apiMessageDto = new ApiMessageDto<>();
//
//        Article articlePage = articleRepository.findById(id).orElse(null);
//        apiMessageDto.setData(articleMapper.fromEntityToDto(articlePage));
//        apiMessageDto.setMessage("List article success");
//        return apiMessageDto;
//    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ArticleAdminDto>> list(ArticleCriteria articleCriteria, Pageable pageable) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<ArticleAdminDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Article> list = articleRepository.findAll(articleCriteria.getSpecification(), pageable);
        ResponseListObj<ArticleAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(articleMapper.fromEntityListToArticleDtoListNoArticleContent(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ArticleAdminDto> get(@PathVariable("id") Long id){
        Account currentUser = accountRepository.findById(getCurrentUser()) .orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(UserBaseConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(UserBaseConstant.USER_KIND_EMPLOYEE)
                && !currentUser.getKind().equals(UserBaseConstant.USER_KIND_COLLABORATOR)) {
            throw new RequestException(ErrorCode.ARTICLE_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<ArticleAdminDto> result = new ApiMessageDto<>();
        Article article = articleRepository.findById(id).orElse(null);
        if(article == null){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_NOT_FOUND);
        }
        if(!currentUser.getKind().equals(UserBaseConstant.USER_KIND_ADMIN)
                && !article.getStatus().equals(UserBaseConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.ARTICLE_ERROR_NOT_FOUND);
        }

        result.setData(articleMapper.fromEntityToArticleDto(article));
        result.setMessage("Get article success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateArticleForm createArticleForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Article article = articleMapper.fromCreateArticleFormToEntity(createArticleForm);

        articleRepository.save(article);
        apiMessageDto.setMessage("Create article success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateArticleForm updateArticleForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Article article = articleRepository.findById(updateArticleForm.getId()).orElse(null);
        if (article == null) {
            throw new RequestException(ErrorCode.ARTICLE_ERROR_NOT_FOUND);
        }

        articleMapper.fromUpdateArticleFormToEntity(updateArticleForm, article);

        if (StringUtils.isNoneBlank(updateArticleForm.getAvatar())) {
            if(!updateArticleForm.getAvatar().equals(article.getAvatar())){
                //delete old image
                landingIsApiService.deleteFile(article.getAvatar());
            }
            article.setAvatar(updateArticleForm.getAvatar());
        }

        articleRepository.save(article);

        apiMessageDto.setMessage("Update article success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();
        Article article = articleRepository.findById(id).orElse(null);
        if(article == null){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_NOT_FOUND);
        }
        landingIsApiService.deleteFile(article.getAvatar());
        articleRepository.delete(article);
        result.setMessage("Delete success");
        return result;
    }

}