package cn.jerryshell.polls.service;

import cn.jerryshell.polls.dao.ChoiceDAO;
import cn.jerryshell.polls.model.Choice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChoiceService {
    private ChoiceDAO choiceDAO;

    @Autowired
    public void setChoiceDAO(ChoiceDAO choiceDAO) {
        this.choiceDAO = choiceDAO;
    }

    public Optional<Choice> findById(Long id) {
        return choiceDAO.findById(id);
    }

    public List<Choice> findByPollId(Long pollId) {
        return choiceDAO.findByPollId(pollId);
    }

    public Choice create(Choice choice) {
        return choiceDAO.save(choice);
    }

    public Choice update(Choice choice) {
        return choiceDAO.save(choice);
    }

    public boolean existsById(Long id) {
        return choiceDAO.existsById(id);
    }

    public void delete(Long id) {
        choiceDAO.deleteById(id);
    }
}
