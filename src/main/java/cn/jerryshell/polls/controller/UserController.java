package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.User;
import cn.jerryshell.polls.payload.UserInfoUpdateForm;
import cn.jerryshell.polls.payload.UserPasswordUpdateForm;
import cn.jerryshell.polls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @GetMapping("/usernameOrEmail/{usernameOrEmail}")
    public User findByUsernameOrEmail(@PathVariable String usernameOrEmail) {
        return userService.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username Or Email", usernameOrEmail));
    }

    @TokenRequired
    @PutMapping
    public User updateUserInfo(@RequestAttribute String username,
                               @Valid @RequestBody UserInfoUpdateForm form) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        user.setEmail(form.getEmail());
        return userService.update(user);
    }

    @TokenRequired
    @PutMapping("/password")
    public User updateUserPassword(@RequestAttribute String username,
                                   @Valid @RequestBody UserPasswordUpdateForm form) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        user.setPassword(form.getPassword());
        return userService.update(user);
    }

}
