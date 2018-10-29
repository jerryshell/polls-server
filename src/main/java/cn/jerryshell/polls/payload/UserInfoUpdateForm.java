package cn.jerryshell.polls.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserInfoUpdateForm {
    @NotBlank
    @Size(max = 64)
    @Email
    private String email;
}
