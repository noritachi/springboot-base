package com.base.auth.dto.quiz;

import com.base.auth.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class QuizAdminDto extends ABasicAdminDto {

    private Long id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
    private Integer kind;

}
