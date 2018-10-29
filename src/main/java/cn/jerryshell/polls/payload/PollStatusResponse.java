package cn.jerryshell.polls.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollStatusResponse {
    private Long choiceId;
    private Long choiceCount;
}
