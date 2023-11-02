package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListObj;
import com.base.auth.dto.quiz.QuizAdminDto;
import com.base.auth.exception.RequestException;
import com.base.auth.form.quiz.CreateQuizForm;
import com.base.auth.form.quiz.UpdateQuizForm;
import com.base.auth.mapper.QuizMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Quiz;
import com.base.auth.model.criteria.QuizCriteria;
import com.base.auth.repository.AccountRepository;
import com.base.auth.repository.QuizRepository;
import com.base.auth.service.LandingIsApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/quiz")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class QuizController extends ABasicController{

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizMapper quizMapper;

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
    public ApiMessageDto<ResponseListObj<QuizAdminDto>> list(QuizCriteria quizCriteria, Pageable pageable) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.QUIZ_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<QuizAdminDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Quiz> list = quizRepository.findAll(quizCriteria.getSpecification(), pageable);
        ResponseListObj<QuizAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(quizMapper.fromEntityListToQuizDtoListNoQuizAnswer(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<QuizAdminDto> get(@PathVariable("id") Long id){
        Account currentUser = accountRepository.findById(getCurrentUser()) .orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(UserBaseConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(UserBaseConstant.USER_KIND_EMPLOYEE)
                && !currentUser.getKind().equals(UserBaseConstant.USER_KIND_COLLABORATOR)) {
            throw new RequestException(ErrorCode.QUIZ_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<QuizAdminDto> result = new ApiMessageDto<>();
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if(quiz == null){
            throw new RequestException(ErrorCode.QUIZ_ERROR_NOT_FOUND);
        }
        if(!currentUser.getKind().equals(UserBaseConstant.USER_KIND_ADMIN)
                && !quiz.getStatus().equals(UserBaseConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.QUIZ_ERROR_NOT_FOUND);
        }

        result.setData(quizMapper.fromEntityToQuizDto(quiz));
        result.setMessage("Get quiz success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateQuizForm createQuizForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.QUIZ_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Quiz quiz = quizMapper.fromCreateQuizFormToEntity(createQuizForm);

        quizRepository.save(quiz);
        apiMessageDto.setMessage("Create quiz success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateQuizForm updateQuizForm, BindingResult bindingResult) {
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.QUIZ_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Quiz quiz = quizRepository.findById(updateQuizForm.getId()).orElse(null);
        if (quiz == null) {
            throw new RequestException(ErrorCode.QUIZ_ERROR_NOT_FOUND);
        }

        quizMapper.fromUpdateQuizFormToEntity(updateQuizForm, quiz);

        quizRepository.save(quiz);

        apiMessageDto.setMessage("Update quiz success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        if(!isSuperAdmin()){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if(quiz == null){
            throw new RequestException(ErrorCode.ARTICLE_ERROR_NOT_FOUND);
        }
        quizRepository.delete(quiz);
        result.setMessage("Delete success");
        return result;
    }

}