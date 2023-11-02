package com.base.auth.mapper;

import com.base.auth.dto.quiz.QuizAdminDto;
import com.base.auth.form.quiz.CreateQuizForm;
import com.base.auth.form.quiz.UpdateQuizForm;
import com.base.auth.model.Quiz;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface QuizMapper {

    @Mapping(source = "question", target = "question")
    @Mapping(source = "option1", target = "option1")
    @Mapping(source = "option2", target = "option2")
    @Mapping(source = "option3", target = "option3")
    @Mapping(source = "option4", target = "option4")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "answer", target = "answer")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateQuizMapping")
    Quiz fromCreateQuizFormToEntity(CreateQuizForm createQuizForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "question", target = "question")
    @Mapping(source = "option1", target = "option1")
    @Mapping(source = "option2", target = "option2")
    @Mapping(source = "option3", target = "option3")
    @Mapping(source = "option4", target = "option4")
    @Mapping(source = "answer", target = "answer")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateQuizMapping")
    void fromUpdateQuizFormToEntity(UpdateQuizForm updateQuizForm, @MappingTarget Quiz quiz);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "question", target = "question")
    @Mapping(source = "option1", target = "option1")
    @Mapping(source = "option2", target = "option2")
    @Mapping(source = "option3", target = "option3")
    @Mapping(source = "option4", target = "option4")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMappingNoQuizAnswer")
    QuizAdminDto fromEntityToQuizDtoNoQuizAnswer(Quiz quiz);

    @IterableMapping(elementTargetType = QuizAdminDto.class, qualifiedByName = "adminGetMappingNoQuizAnswer")
    List<QuizAdminDto> fromEntityListToQuizDtoListNoQuizAnswer(List<Quiz> quiz);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "question", target = "question")
    @Mapping(source = "option1", target = "option1")
    @Mapping(source = "option2", target = "option2")
    @Mapping(source = "option3", target = "option3")
    @Mapping(source = "option4", target = "option4")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "answer", target = "answer")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    QuizAdminDto fromEntityToQuizDto(Quiz quiz);
}
