package com.base.auth.form.quiz;

import com.base.auth.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateQuizForm {

    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @NotEmpty(message = "title cannot be null")
    @ApiModelProperty(required = true)
    private String question;

    @NotEmpty(message = "option1 cannot be null")
    @ApiModelProperty(required = true)
    private String option1;

    @NotEmpty(message = "option2 cannot be null")
    @ApiModelProperty(required = true)
    private String option2;

    @NotEmpty(message = "option3 cannot be null")
    @ApiModelProperty(required = true)
    private String option3;

    @NotEmpty(message = "option4 cannot be null")
    @ApiModelProperty(required = true)
    private String option4;

    @NotEmpty(message = "answer cannot be null")
    @ApiModelProperty(required = true)
    private String answer;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    @Status
    private Integer status;
}
