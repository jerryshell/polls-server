package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.RoleRequired;
import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.dao.UserDAO;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.Role;
import cn.jerryshell.polls.model.User;
import cn.jerryshell.polls.payload.UserInfoUpdateForm;
import cn.jerryshell.polls.payload.UserPasswordUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/username/{username}")
    public User findUserByUsername(@PathVariable String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @GetMapping("/usernameOrEmail/{usernameOrEmail}")
    public User findByUsernameOrEmail(@PathVariable String usernameOrEmail) {
        return userDAO.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username Or Email", usernameOrEmail));
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_VIP, Role.ROLE_ADMIN})
    @GetMapping("/vip")
    public User vip(@RequestAttribute String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @TokenRequired
    @RoleRequired(roles = Role.ROLE_ADMIN)
    @GetMapping("/admin")
    public User admin(@RequestAttribute String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @TokenRequired
    @PutMapping
    public User updateUserInfo(@RequestAttribute String username,
                               @Valid @RequestBody UserInfoUpdateForm form) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        user.setEmail(form.getEmail());
        return userDAO.save(user);
    }

    @TokenRequired
    @PutMapping("/password")
    public User updateUserPassword(@RequestAttribute String username,
                                   @Valid @RequestBody UserPasswordUpdateForm form) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        user.setPassword(form.getPassword());
        return userDAO.save(user);
    }

}
