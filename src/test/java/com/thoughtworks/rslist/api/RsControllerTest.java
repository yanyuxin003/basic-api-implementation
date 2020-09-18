package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;
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
    public void should_add_rs_event_has_no_user() throws Exception {
        User user = new User("mali", "male", 20, "ab@thoughtworks.com", "10088888888");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "食品", 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord", is("食品")))
                .andExpect(jsonPath("$[3].user.name", is("mali")))
                .andExpect(jsonPath("$[3].user.gender", is("male")))
                .andExpect(jsonPath("$[3].user.age", is(20)))
                .andExpect(jsonPath("$[3].user.email", is("ab@thoughtworks.com")))
                .andExpect(jsonPath("$[3].user.phone", is("10088888888")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    //validation homework 3：如果userName已存在在user列表中的话则只需添加热搜事件到热搜事件列表，
    public void should_add_rs_event_has_user() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        //  RsEvent rsEvent = new RsEvent("武汉解封了", "新闻", user);
        RsEvent rsEvent = new RsEvent("武汉解封了", "新闻", 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().stringValues("index", "3"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[3].eventName", is("武汉解封了")))
                .andExpect(jsonPath("$[3].keyWord", is("新闻")))
                .andExpect(jsonPath("$[3].user.name", is("xiaowang")))
                .andExpect(jsonPath("$[3].user.gender", is("female")))
                .andExpect(jsonPath("$[3].user.age", is(19)))
                .andExpect(jsonPath("$[3].user.email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$[3].user.phone", is("18888888888")))
                .andExpect(status().isOk());
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

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_has_user() throws Exception {
        UserPO saveUser = userRepository.save(UserPO.builder().name("yyx").age(20).
                gender("female").email("a@thoughtworks.com").phone("10088888888").voteNum(10).build());

        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":" + saveUser.getId() + "}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(2, all.size());
        assertEquals("猪肉涨价了", all.get(0).getEventName());
        assertEquals("经济", all.get(0).getKeyWord());
        assertEquals(saveUser.getId(), all.get(0).getId());
    }

    @DirtiesContext
    @Test
    public void should_add_rs_event_when_not_has_user() throws Exception {

        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\":" + 100 + "}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }


    @Test
    public void should_update_rsEvent_when_rsEventId_equal_userId() throws Exception {
        RsEventPO oldRsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("猪肉")
                .keyWord("经济").userId(1).build());
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
                .keyWord("经济").userId(1).build());
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
                .keyWord("经济").userId(1).build());
        String jsonString = "{\"eventName\":null,\"keyWord\":\"奇观\",\"userId\": " + 1 + "}";

        mockMvc.perform(patch("/rs/{rsEventId}", String.valueOf(oldRsEventPO.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventPO newRsEvent = rsEventRepository.findById(oldRsEventPO.getId()).get();
        assertEquals("猪肉", newRsEvent.getEventName());
        assertEquals("奇观", newRsEvent.getKeyWord());
    }
}
