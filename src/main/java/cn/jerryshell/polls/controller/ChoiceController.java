package cn.jerryshell.polls.controller;

import cn.jerryshell.polls.annotation.RoleRequired;
import cn.jerryshell.polls.annotation.TokenRequired;
import cn.jerryshell.polls.dao.ChoiceDAO;
import cn.jerryshell.polls.dao.PollDAO;
import cn.jerryshell.polls.exception.ResourceNotFoundException;
import cn.jerryshell.polls.model.Choice;
import cn.jerryshell.polls.model.Poll;
import cn.jerryshell.polls.model.Role;
import cn.jerryshell.polls.payload.CreateNewChoiceForm;
import cn.jerryshell.polls.payload.UpdateChoiceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/polls/{pollId}/choices")
public class ChoiceController {
    private PollDAO pollDAO;
    private ChoiceDAO choiceDAO;

    @Autowired
    public void setPollDAO(PollDAO pollDAO) {
        this.pollDAO = pollDAO;
    }

    @Autowired
    public void setChoiceDAO(ChoiceDAO choiceDAO) {
        this.choiceDAO = choiceDAO;
    }

    @GetMapping
    public List<Choice> findByPollId(@PathVariable Long pollId) {
        return choiceDAO.findByPollId(pollId);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @PostMapping
    public Choice createNewChoice(@PathVariable Long pollId,
                                  @Valid @RequestBody CreateNewChoiceForm form) {
        Poll poll = pollDAO.findById(pollId)
                .orElseThrow(() -> ResourceNotFoundException.build("Poll", "ID", pollId));

        Choice choice = new Choice();
        choice.setText(form.getText());
        choice.setPoll(poll);
        return choiceDAO.save(choice);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @PutMapping("/{id}")
    public Choice updateChoice(@PathVariable Long pollId,
                               @PathVariable Long id,
                               @Valid @RequestBody UpdateChoiceForm form) {
        if (!pollDAO.existsById(pollId)) {
            throw ResourceNotFoundException.build("Poll", "ID", pollId);
        }

        Choice choice = choiceDAO.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.build("Choice", "ID", id));

        choice.setText(form.getText());
        return choiceDAO.save(choice);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChoice(@PathVariable Long pollId,
                                          @PathVariable Long id) {
        if (!pollDAO.existsById(pollId)) {
            throw ResourceNotFoundException.build("Poll", "ID", pollId);
        }

        if (!choiceDAO.existsById(id)) {
            throw ResourceNotFoundException.build("Choice", "ID", id);
        }

        choiceDAO.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
