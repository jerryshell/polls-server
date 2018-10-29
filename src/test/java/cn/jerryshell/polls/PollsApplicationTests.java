package cn.jerryshell.polls;

import cn.jerryshell.polls.payload.CreateNewPollForm;
import cn.jerryshell.polls.payload.LoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PollsApplicationTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testAuthControllerAndUserController() throws Exception {
        // find
        mockMvc.perform(get("/users/username/admin"))
                .andExpect(jsonPath("$.username").value("admin"));
        mockMvc.perform(get("/users/usernameOrEmail/admin@email.com"))
                .andExpect(jsonPath("$.username").value("admin"));
        mockMvc.perform(get("/users/usernameOrEmail/none"))
                .andExpect(status().isNotFound());

        // login
        JSONObject loginForm = new JSONObject();
        loginForm.put("username", "admin");
        loginForm.put("password", "123");
        String token = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(loginForm.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        mockMvc.perform(get("/auth/verify").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));

        // register
        JSONObject registerForm = new JSONObject();
        registerForm.put("username", "new_user");
        registerForm.put("password", "111");
        registerForm.put("password2", "111");
        registerForm.put("email", "new_user@email.com");
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON_UTF8).content(registerForm.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_user"));
    }

    @Test
    public void testPollController() throws Exception {
        // find
        mockMvc.perform(get("/polls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        mockMvc.perform(get("/polls/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("One or Two"));

        // status
        mockMvc.perform(get("/polls/1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].choiceId").value(1))
                .andExpect(jsonPath("$[0].choiceCount").value(2));

        ObjectMapper objectMapper = new ObjectMapper();

        // get token
        LoginForm loginForm = new LoginForm();
        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // create new poll
        CreateNewPollForm createNewPollForm = new CreateNewPollForm();
        createNewPollForm.setQuestion("new question");
        mockMvc.perform(post("/polls")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(createNewPollForm))
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

}
