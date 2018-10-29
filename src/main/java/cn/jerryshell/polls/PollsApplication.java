package cn.jerryshell.polls;

import cn.jerryshell.polls.dao.ChoiceDAO;
import cn.jerryshell.polls.dao.PollDAO;
import cn.jerryshell.polls.dao.UserDAO;
import cn.jerryshell.polls.dao.VoteDAO;
import cn.jerryshell.polls.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class PollsApplication implements CommandLineRunner {

    private UserDAO userDAO;
    private PollDAO pollDAO;
    private ChoiceDAO choiceDAO;
    private VoteDAO voteDAO;

    public static void main(String[] args) {
        SpringApplication.run(PollsApplication.class, args);
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPollDAO(PollDAO pollDAO) {
        this.pollDAO = pollDAO;
    }

    @Autowired
    public void setChoiceDAO(ChoiceDAO choiceDAO) {
        this.choiceDAO = choiceDAO;
    }

    @Autowired
    public void setVoteDAO(VoteDAO voteDAO) {
        this.voteDAO = voteDAO;
    }

    @Override
    public void run(String... args) throws Exception {
        initUserData();
        initPollData();
        initChoiceData();
        initVoteData();
    }

    private void initVoteData() {
        User admin = userDAO.findByUsername("admin")
                .orElseThrow(RuntimeException::new);
        User vip = userDAO.findByUsername("vip")
                .orElseThrow(RuntimeException::new);
        User user = userDAO.findByUsername("user")
                .orElseThrow(RuntimeException::new);
        Poll poll = pollDAO.findByUser(admin).get(0);
        assert poll != null;
        List<Choice> choiceList = choiceDAO.findByPollId(poll.getId());
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

        voteDAO.save(vote1);
        voteDAO.save(vote2);
    }

    private void initChoiceData() {
        User admin = userDAO.findByUsername("admin")
                .orElseThrow(RuntimeException::new);
        Poll poll = pollDAO.findByUser(admin).get(0);
        assert poll != null;

        Choice choiceOne = new Choice();
        choiceOne.setPoll(poll);
        choiceOne.setText("One");

        Choice choiceTwo = new Choice();
        choiceTwo.setPoll(poll);
        choiceTwo.setText("Two");

        choiceDAO.save(choiceOne);
        choiceDAO.save(choiceTwo);
    }

    private void initPollData() {
        User admin = userDAO.findByUsername("admin")
                .orElseThrow(RuntimeException::new);

        Poll poll = new Poll();
        poll.setQuestion("One or Two");
        poll.setUser(admin);

        pollDAO.save(poll);
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

        userDAO.save(admin);
        userDAO.save(vip);
        userDAO.save(user);
    }
}
