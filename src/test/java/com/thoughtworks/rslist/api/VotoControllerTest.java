package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VotoControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

    UserPO userPO;
    RsEventPO rsEventPO;
    VotePO votePO;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();

        userPO = userRepository.save(UserPO.builder().email("lisa@b.com").age(19).gender("female")
                .phone("18888888888").name("lisa").voteNum(10).build());
        rsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("猪肉上涨了")
                .keyWord("经济").userPO(userPO).voteNum(0).build());


    }

    @Test
    public void should_get_vote_record() throws Exception {
        for (int i = 0; i< 8; i++){
            votePO = voteRepository.save(VotePO.builder().rsEvent(rsEventPO).voteNum(i+1).user(userPO)
                    .localDateTime(LocalDateTime.now()).build());
        }


        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPO.getId()))
                .param("rsEventId", String.valueOf(rsEventPO.getId()))
                .param("pageIndex","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteCount",is(1)))
                .andExpect(jsonPath("$[1].voteCount",is(2)))
                .andExpect(jsonPath("$[2].voteCount",is(3)))
                .andExpect(jsonPath("$[3].voteCount",is(4)))
                .andExpect(jsonPath("$[4].voteCount",is(5)));

        mockMvc.perform(get("/voteRecord")
                .param("userId",String.valueOf(userPO.getId()))
                .param("rsEventId",String.valueOf(rsEventPO.getId()))
                .param("pageIndex","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteCount",is(6)))
                .andExpect(jsonPath("$[1].voteCount",is(7)))
                .andExpect(jsonPath("$[2].voteCount",is(8)));

    }

   
}
