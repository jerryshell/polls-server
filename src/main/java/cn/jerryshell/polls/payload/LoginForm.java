package cn.jerryshell.polls.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginForm {
    @NotBlank
    @Size(max = 16)
    private String username;

    @NotBlank
    @Size(max = 128)
    private String password;
}
