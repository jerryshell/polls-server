package cn.jerryshell.polls.service;

import cn.jerryshell.polls.dao.VoteDAO;
import cn.jerryshell.polls.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private VoteDAO voteDAO;

    @Autowired
    public void setVoteDAO(VoteDAO voteDAO) {
        this.voteDAO = voteDAO;
    }

    public Vote create(Vote vote) {
        return voteDAO.save(vote);
    }

    public Long countByChoiceId(Long choiceId) {
        return voteDAO.countByChoiceId(choiceId);
    }

    public boolean existsByPollIdAndUsername(Long pollId, String username) {
        return voteDAO.existsByPollIdAndUser_Username(pollId, username);
    }
}
