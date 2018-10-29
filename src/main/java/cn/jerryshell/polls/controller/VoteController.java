package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.RoleRequired;
import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.dao.ChoiceDAO;
import cn.jerryshell.polls.dao.UserDAO;
import cn.jerryshell.polls.dao.VoteDAO;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.*;
import cn.jerryshell.polls.payload.CreateNewVoteForm;
import cn.jerryshell.polls.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/polls/{pollId}/votes")
public class VoteController {
    private PollService pollService;

    private VoteDAO voteDAO;
    private UserDAO userDAO;
    private ChoiceDAO choiceDAO;

    @Autowired
    public VoteController(VoteDAO voteDAO, UserDAO userDAO, ChoiceDAO choiceDAO) {
        this.voteDAO = voteDAO;
        this.userDAO = userDAO;
        this.choiceDAO = choiceDAO;
    }

    @Autowired
    public void setPollService(PollService pollService) {
        this.pollService = pollService;
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_USER, Role.ROLE_ADMIN})
    @PostMapping
    public Vote createNewVote(@PathVariable Long pollId,
                              @RequestAttribute String username,
                              @RequestBody CreateNewVoteForm form) {
        if (voteDAO.existsByPollIdAndUser_Username(pollId, username)) {
            throw new RuntimeException("不能重复投票");
        }

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        Poll poll = pollService.findById(pollId)
                .orElseThrow(() -> ResourceNotFoundException.build("Poll", "ID", pollId));
        Choice choice = choiceDAO.findById(form.getChoiceId())
                .orElseThrow(() -> ResourceNotFoundException.build("Choice", "ID", form.getChoiceId()));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPoll(poll);
        vote.setChoice(choice);

        return voteDAO.save(vote);
    }
}
