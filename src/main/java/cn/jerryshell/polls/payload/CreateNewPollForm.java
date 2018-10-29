package cn.jerryshell.polls.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateNewPollForm {
    @NotBlank
    private String question;
}
