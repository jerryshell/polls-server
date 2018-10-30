package cn.jerryshell.polls.dao;

import cn.jerryshell.polls.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollDAO extends JpaRepository<Poll, Long> {
    List<Poll> findByUserId(Long userId);
}
