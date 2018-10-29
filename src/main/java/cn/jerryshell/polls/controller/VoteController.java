package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.RoleRequired;
import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.dao.UserDAO;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.*;
import cn.jerryshell.polls.payload.CreateNewVoteForm;
import cn.jerryshell.polls.service.ChoiceService;
import cn.jerryshell.polls.service.PollService;
import cn.jerryshell.polls.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/polls/{pollId}/votes")
public class VoteController {
    private PollService pollService;
    private ChoiceService choiceService;
    private VoteService voteService;

    // TODO
    private UserDAO userDAO;

    @Autowired
    public VoteController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPollService(PollService pollService) {
        this.pollService = pollService;
    }

    @Autowired
    public void setChoiceService(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    @Autowired
    public void setVoteService(VoteService voteService) {
        this.voteService = voteService;
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_USER, Role.ROLE_ADMIN})
    @PostMapping
    public Vote createNewVote(@PathVariable Long pollId,
                              @RequestAttribute String username,
                              @RequestBody CreateNewVoteForm form) {
        if (voteService.existsByPollIdAndUsername(pollId, username)) {
            throw new RuntimeException("不能重复投票");
        }

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        Poll poll = pollService.findById(pollId)
                .orElseThrow(() -> ResourceNotFoundException.build("Poll", "ID", pollId));
        Choice choice = choiceService.findById(form.getChoiceId())
                .orElseThrow(() -> ResourceNotFoundException.build("Choice", "ID", form.getChoiceId()));

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPoll(poll);
        vote.setChoice(choice);

        return voteService.create(vote);
    }
}
