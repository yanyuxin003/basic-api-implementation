package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    UserPO userPO;
    RsEventPO rsEventPO;


    @DirtiesContext
    @Test
    void should_get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                // .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                // .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                // .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    void should_get_one_rs_event() throws Exception {
        User user = new User("mali", "male", 20, "ab@thoughtworks.com", "10088888888");
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                // .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                // .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                // .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    void should_get_rs_event_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }


    @DirtiesContext
    @Test
    public void should_add_rs_event_when_user_exist() throws Exception {
        UserPO saveUser = userRepository.save(UserPO.builder().name("zhangsan").age(20).
                gender("female").email("a@thoughtworks.com").phone("10088888888").voteNum(10).build());

        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":" + saveUser.getId() + "}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals("猪肉涨价了", all.get(0).getEventName());
        assertEquals("经济", all.get(0).getKeyWord());
        assertEquals(saveUser.getId(), all.get(0).getUserPO().getId());
    }

    @Test
    @Order(2)
    void should_throw_exception_when_rsEventId_invalid() throws Exception {
        mockMvc.perform(get("/rs/{rsEventId}", String.valueOf(100)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @DirtiesContext
    @Test
    public void should_add_new_rsEvent_when_user_exists() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": " + userPO.getId()+ " }";
        mockMvc.perform(post("/rs/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_user_not_exist() throws Exception {

        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":" + 10 + "}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @DirtiesContext
    @Test
    void should_update_event_given_two_fields() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("武汉解封了", "新闻", 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/event/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("武汉解封了")))
                .andExpect(jsonPath("$.keyWord", is("新闻")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    void should_update_event_given_one_field() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("猪上树了", null, 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/event/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("猪上树了")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());

        RsEvent rsEvent1 = new RsEvent(null, "奇观", 1);
        ObjectMapper objectMapper1 = new ObjectMapper();
        String jsonString1 = objectMapper1.writeValueAsString(rsEvent1);
        mockMvc.perform(patch("/rs/event/2").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("猪上树了")))
                .andExpect(jsonPath("$.keyWord", is("奇观")))
                .andExpect(status().isOk());


    }

    @DirtiesContext
    @Test
    void should_delete_event_given_index() throws Exception {
        mockMvc.perform(delete("/rs/event/1")).andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }


    @Test
    public void should_update_rsEvent_when_rsEventId_equal_userId() throws Exception {
        RsEventPO oldRsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("猪肉")
                .keyWord("经济").userPO(userPO).build());
        String jsonString = "{\"eventName\":\"猪上树了\",\"keyWord\":\"奇观\",\"userId\": " + 1 + "}";
        mockMvc.perform(patch("/rs/{rsEventId}", String.valueOf(oldRsEventPO.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventPO newRsEvent = rsEventRepository.findById(oldRsEventPO.getId()).get();
        assertEquals("猪上树了", newRsEvent.getEventName());
        assertEquals("奇观", newRsEvent.getKeyWord());
    }

    @Test
    public void should_update_rsEvent_when_only_eventName() throws Exception {
        RsEventPO oldRsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("猪肉涨价了")
                .keyWord("经济").userPO(userPO).build());
        String jsonString = "{\"eventName\":\"武汉解封了\",\"keyWord\":null,\"userId\": " + 1 + "}";

        mockMvc.perform(patch("/rs/{rsEventId}", String.valueOf(oldRsEventPO.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventPO newRsEvent = rsEventRepository.findById(oldRsEventPO.getId()).get();
        assertEquals("武汉解封了", newRsEvent.getEventName());
        assertEquals("经济", newRsEvent.getKeyWord());
    }

    @Test
    public void should_update_rsEvent_when_only_keyword() throws Exception {
        RsEventPO oldRsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("猪肉涨价了")
                .keyWord("经济").userPO(userPO).build());
        String jsonString = "{\"eventName\":null,\"keyWord\":\"奇观\",\"userId\": " + 1 + "}";

        mockMvc.perform(patch("/rs/{rsEventId}", String.valueOf(oldRsEventPO.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventPO newRsEvent = rsEventRepository.findById(oldRsEventPO.getId()).get();
        assertEquals("猪肉涨价了", newRsEvent.getEventName());
        assertEquals("奇观", newRsEvent.getKeyWord());
    }
}
