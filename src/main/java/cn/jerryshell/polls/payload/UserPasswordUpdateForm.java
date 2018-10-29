package cn.jerryshell.polls.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserPasswordUpdateForm {
    @NotBlank
    @Size(max = 128)
    private String password;
}
