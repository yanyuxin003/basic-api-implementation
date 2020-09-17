package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.is;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;

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
    //validation homework 3 如果userName不存在，则将User添加到热搜事件列表中（相当于注册用户）
    public void should_add_rs_event_has_no_user() throws Exception {
        User user = new User("mali", "male", 20, "ab@thoughtworks.com", "10088888888");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "食品", user);
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
        RsEvent rsEvent = new RsEvent("武汉解封了", "新闻", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().stringValues("index","3"))
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
        RsEvent rsEvent = new RsEvent("武汉解封了","新闻",user);
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
        RsEvent rsEvent = new RsEvent("猪上树了",null,user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/event/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("猪上树了")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());

        RsEvent rsEvent1 = new RsEvent(null,"奇观",user);
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
        mockMvc.perform(delete("/rs/event/1")) .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无关键字")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }
}
