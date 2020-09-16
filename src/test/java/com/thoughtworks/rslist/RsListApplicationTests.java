package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;

import domain.RsEvent;
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
    void should_get_rs_event_list() throws Exception{
        mockMvc.perform(get("/rs/list"))
                //数组元素个数3
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
    void should_get_one_rs_event() throws Exception{
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(status().isOk());
    }

    @DirtiesContext
    @Test
    void should_get_rs_event_between() throws Exception{
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
    public void should_add_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价了","食品");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

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
                .andExpect(status().isOk());
    }
    @DirtiesContext
    @Test
    void should_update_event_given_two_fields() throws Exception {
        RsEvent rsEvent = new RsEvent("疫情开始了", "社会");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/event/2").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("疫情开始了")))
                .andExpect(jsonPath("$.keyWord", is("社会")))
                .andExpect(status().isOk());
    }
    @DirtiesContext
    @Test
    void should_update_event_given_one_field() throws Exception {
        RsEvent firstTestEvent = new RsEvent();
        firstTestEvent.setKeyWord("体育");
        ObjectMapper objectMapper = new ObjectMapper();
        String firstJson = objectMapper.writeValueAsString(firstTestEvent);

        mockMvc.perform(patch("/rs/event/3").content(firstJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("体育")))
                .andExpect(status().isOk());

        RsEvent secondTestEvent = new RsEvent();
        secondTestEvent.setEventName("中国足球队冠军");
        String secondJson = objectMapper.writeValueAsString(secondTestEvent);

        mockMvc.perform(patch("/rs/event/3").content(secondJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("中国足球队冠军")))
                .andExpect(jsonPath("$.keyWord", is("体育")))
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
}
