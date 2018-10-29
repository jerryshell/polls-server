package cn.jerryshell.polls.service;

import cn.jerryshell.polls.dao.PollDAO;
import cn.jerryshell.polls.model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PollService {
    private PollDAO pollDAO;

    @Autowired
    public void setPollDAO(PollDAO pollDAO) {
        this.pollDAO = pollDAO;
    }

    public Optional<Poll> findById(Long id) {
        return pollDAO.findById(id);
    }

    public List<Poll> findAll() {
        return pollDAO.findAll();
    }

    public Poll create(Poll poll) {
        return pollDAO.save(poll);
    }

    public Poll update(Poll poll) {
        return pollDAO.save(poll);
    }

    public void delete(Long id) {
        pollDAO.deleteById(id);
    }

    public boolean existsById(Long id) {
        return pollDAO.existsById(id);
    }
}
