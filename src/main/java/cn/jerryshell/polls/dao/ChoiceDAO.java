package cn.jerryshell.polls.dao;

import cn.jerryshell.polls.model.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceDAO extends JpaRepository<Choice, Long> {
    List<Choice> findByPollId(Long pollId);
}
