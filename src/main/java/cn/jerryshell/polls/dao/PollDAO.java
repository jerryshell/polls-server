package cn.jerryshell.polls.dao;

import cn.jerryshell.polls.model.Poll;
import cn.jerryshell.polls.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollDAO extends JpaRepository<Poll, Long> {
    List<Poll> findByUser(User user);
}
