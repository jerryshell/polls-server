package cn.jerryshell.polls.service;

import cn.jerryshell.polls.dao.UserDAO;
import cn.jerryshell.polls.model.User;
import cn.jerryshell.polls.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userDAO.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        password = MD5Util.MD5Base64(password);
        return userDAO.findByUsernameAndPassword(username, password);
    }

    public User update(User user) {
        user.setPassword(MD5Util.MD5Base64(user.getPassword()));
        return userDAO.save(user);
    }

    public User create(User user) {
        user.setPassword(MD5Util.MD5Base64(user.getPassword()));
        return userDAO.save(user);
    }

    public boolean existsByUsername(String username) {
        return userDAO.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }
}
