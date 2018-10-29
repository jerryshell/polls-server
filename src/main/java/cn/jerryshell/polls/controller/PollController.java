package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.RoleRequired;
import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.dao.ChoiceDAO;
import cn.jerryshell.polls.dao.UserDAO;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.Choice;
import cn.jerryshell.polls.model.Poll;
import cn.jerryshell.polls.model.Role;
import cn.jerryshell.polls.model.User;
import cn.jerryshell.polls.payload.CreateNewPollForm;
import cn.jerryshell.polls.payload.PollStatusResponse;
import cn.jerryshell.polls.payload.UpdatePollForm;
import cn.jerryshell.polls.service.PollService;
import cn.jerryshell.polls.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/polls")
public class PollController {
    private PollService pollService;
    private VoteService voteService;

    private UserDAO userDAO;
    private ChoiceDAO choiceDAO;

    @Autowired
    public void setPollService(PollService pollService) {
        this.pollService = pollService;
    }

    @Autowired
    public void setVoteService(VoteService voteService) {
        this.voteService = voteService;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setChoiceDAO(ChoiceDAO choiceDAO) {
        this.choiceDAO = choiceDAO;
    }

    @GetMapping("/{id}")
    public Poll findById(@PathVariable Long id) {
        return pollService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.build("Poll", "ID", id));
    }

    @GetMapping
    public List<Poll> findAll() {
        return pollService.findAll();
    }

    @GetMapping("/{id}/status")
    public List<PollStatusResponse> status(@PathVariable Long id) {
        if (!pollService.existsById(id)) {
            throw ResourceNotFoundException.build("Poll", "ID", id);
        }

        List<PollStatusResponse> responseList = new ArrayList<>();
        List<Choice> choiceList = choiceDAO.findByPollId(id);
        for (Choice choice : choiceList) {
            Long count = voteService.countByChoiceId(choice.getId());

            PollStatusResponse pollStatusResponse = new PollStatusResponse();
            pollStatusResponse.setChoiceId(choice.getId());
            pollStatusResponse.setChoiceCount(count);

            responseList.add(pollStatusResponse);
        }
        return responseList;
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @PostMapping
    public Poll createNewPoll(@RequestAttribute String username,
                              @Valid @RequestBody CreateNewPollForm form) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));

        Poll poll = new Poll();
        poll.setQuestion(form.getQuestion());
        poll.setUser(user);
        return pollService.create(poll);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @PutMapping("/{id}")
    public Poll updatePoll(@PathVariable Long id,
                           @Valid @RequestBody UpdatePollForm form) {
        Poll poll = pollService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.build("Poll", "ID", id));

        poll.setQuestion(form.getQuestion());
        return pollService.update(poll);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePoll(@PathVariable Long id) {
        if (!pollService.existsById(id)) {
            throw ResourceNotFoundException.build("Poll", "ID", id);
        }
        pollService.delete(id);
        return ResponseEntity.ok().build();
    }
}
