package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.RoleRequired;
import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.dao.ChoiceDAO;
import cn.jerryshell.polls.dao.PollDAO;
import cn.jerryshell.polls.dao.UserDAO;
import cn.jerryshell.polls.dao.VoteDAO;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.*;
import cn.jerryshell.polls.payload.CreateNewVoteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/polls/{pollId}/votes")
public class VoteController {
    private VoteDAO voteDAO;
    private PollDAO pollDAO;
    private UserDAO userDAO;
    private ChoiceDAO choiceDAO;

    @Autowired
    public VoteController(VoteDAO voteDAO, PollDAO pollDAO, UserDAO userDAO, ChoiceDAO choiceDAO) {
        this.voteDAO = voteDAO;
        this.pollDAO = pollDAO;
        this.userDAO = userDAO;
        this.choiceDAO = choiceDAO;
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_USER, Role.ROLE_ADMIN})
    @PostMapping
    public Vote createNewVote(@PathVariable Long pollId,
                              @RequestAttribute String username,
                              @RequestBody CreateNewVoteForm createNewVoteForm) {
        if (voteDAO.existsByPollIdAndUser_Username(pollId, username)) {
            throw new RuntimeException("不能重复投票");
        }

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        Poll poll = pollDAO.findById(pollId)
                .orElseThrow(() -> ResourceNotFoundException.build("Poll", "ID", pollId));
        Choice choice = choiceDAO.findById(createNewVoteForm.getChoiceId())
                .orElseThrow(() -> ResourceNotFoundException.build("Choice", "ID", createNewVoteForm.getChoiceId()));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPoll(poll);
        vote.setChoice(choice);

        return voteDAO.save(vote);
    }
}
