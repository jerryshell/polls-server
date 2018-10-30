package cn.jerryshell.polls;

import cn.jerryshell.polls.model.*;
import cn.jerryshell.polls.service.ChoiceService;
import cn.jerryshell.polls.service.PollService;
import cn.jerryshell.polls.service.UserService;
import cn.jerryshell.polls.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class PollsApplication implements CommandLineRunner {

    private UserService userService;
    private PollService pollService;
    private ChoiceService choiceService;
    private VoteService voteService;

    public static void main(String[] args) {
        SpringApplication.run(PollsApplication.class, args);
    }

    @Autowired
    public void setUserService(UserService userDAO) {
        this.userService = userDAO;
    }

    @Autowired
    public void setPollService(PollService pollDAO) {
        this.pollService = pollDAO;
    }

    @Autowired
    public void setChoiceService(ChoiceService choiceDAO) {
        this.choiceService = choiceDAO;
    }

    @Autowired
    public void setVoteService(VoteService voteDAO) {
        this.voteService = voteDAO;
    }

    @Override
    public void run(String... args) {
        initUserData();
        initPollData();
        initChoiceData();
        initVoteData();
    }

    private void initVoteData() {
        User admin = userService.findByUsername("admin")
                .orElseThrow(RuntimeException::new);
        User vip = userService.findByUsername("vip")
                .orElseThrow(RuntimeException::new);
        User user = userService.findByUsername("user")
                .orElseThrow(RuntimeException::new);
        Poll poll = pollService.findByUserId(admin.getId()).get(0);
        assert poll != null;
        List<Choice> choiceList = choiceService.findByPollId(poll.getId());
        assert choiceList.size() == 2;
        Choice choiceOne = choiceList.get(0);

        Vote vote1 = new Vote();
        vote1.setUser(vip);
        vote1.setChoice(choiceOne);
        vote1.setPoll(poll);

        Vote vote2 = new Vote();
        vote2.setUser(user);
        vote2.setChoice(choiceOne);
        vote2.setPoll(poll);

        voteService.create(vote1);
        voteService.create(vote2);
    }

    private void initChoiceData() {
        User admin = userService.findByUsername("admin")
                .orElseThrow(RuntimeException::new);
        Poll poll = pollService.findByUserId(admin.getId()).get(0);
        assert poll != null;

        Choice choiceOne = new Choice();
        choiceOne.setPoll(poll);
        choiceOne.setText("One");

        Choice choiceTwo = new Choice();
        choiceTwo.setPoll(poll);
        choiceTwo.setText("Two");

        choiceService.create(choiceOne);
        choiceService.create(choiceTwo);
    }

    private void initPollData() {
        User admin = userService.findByUsername("admin")
                .orElseThrow(RuntimeException::new);

        Poll poll = new Poll();
        poll.setQuestion("One or Two");
        poll.setUser(admin);

        pollService.create(poll);
    }

    private void initUserData() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("123");
        admin.setEmail("admin@email.com");
        admin.setRole(Role.ROLE_ADMIN);

        User vip = new User();
        vip.setUsername("vip");
        vip.setPassword("456");
        vip.setEmail("vip@email.com");
        vip.setRole(Role.ROLE_VIP);

        User user = new User();
        user.setUsername("user");
        user.setPassword("789");
        user.setEmail("user@email.com");
        user.setRole(Role.ROLE_USER);

        userService.create(admin);
        userService.create(vip);
        userService.create(user);
    }
}
