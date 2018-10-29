package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.RoleRequired;
import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.Choice;
import cn.jerryshell.polls.model.Poll;
import cn.jerryshell.polls.model.Role;
import cn.jerryshell.polls.payload.CreateNewChoiceForm;
import cn.jerryshell.polls.payload.UpdateChoiceForm;
import cn.jerryshell.polls.service.ChoiceService;
import cn.jerryshell.polls.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/polls/{pollId}/choices")
public class ChoiceController {
    private PollService pollService;
    private ChoiceService choiceService;

    @Autowired
    public void setPollService(PollService pollService) {
        this.pollService = pollService;
    }

    @Autowired
    public void setChoiceService(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    @GetMapping
    public List<Choice> findByPollId(@PathVariable Long pollId) {
        return choiceService.findByPollId(pollId);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @PostMapping
    public Choice createNewChoice(@PathVariable Long pollId,
                                  @Valid @RequestBody CreateNewChoiceForm form) {
        Poll poll = pollService.findById(pollId)
                .orElseThrow(() -> ResourceNotFoundException.build("Poll", "ID", pollId));

        Choice choice = new Choice();
        choice.setText(form.getText());
        choice.setPoll(poll);
        return choiceService.create(choice);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @PutMapping("/{id}")
    public Choice updateChoice(@PathVariable Long pollId,
                               @PathVariable Long id,
                               @Valid @RequestBody UpdateChoiceForm form) {
        if (!pollService.existsById(pollId)) {
            throw ResourceNotFoundException.build("Poll", "ID", pollId);
        }

        Choice choice = choiceService.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.build("Choice", "ID", id));

        choice.setText(form.getText());
        return choiceService.update(choice);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChoice(@PathVariable Long pollId,
                                          @PathVariable Long id) {
        if (!pollService.existsById(pollId)) {
            throw ResourceNotFoundException.build("Poll", "ID", pollId);
        }

        if (!choiceService.existsById(id)) {
            throw ResourceNotFoundException.build("Choice", "ID", id);
        }

        choiceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
