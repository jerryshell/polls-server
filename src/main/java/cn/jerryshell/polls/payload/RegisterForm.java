package cn.jerryshell.polls.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterForm extends LoginForm {
    @NotBlank
    @Size(max = 128)
    private String password2;

    @NotBlank
    @Size(max = 64)
    @Email
    private String email;
}
