package com.thoughtworks.rslist;

import domain.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RsUserControllerTest {
    //如果userName已存在在user列表中的话则只需添加热搜事件到热搜事件列表，
    //如果userName不存在，则将User添加到热搜事件列表中（相当于注册用户）
    @Autowired
    MockMvc mockMvc;

    @DirtiesContext
    @Test
    @Order(1)
    void should_get_new_one_rs_event() throws Exception{
        mockMvc.perform(get("/newrs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(jsonPath("$.user", is("[xiaoli,female,19,a@thoughtworks.com,18888888888]")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/newrs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(jsonPath("$.user", is("[xiaowang,male,16,aB@thoughtworks.com,18888889999]")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/newrs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无关键字")))
                .andExpect(jsonPath("$.user", is("[zhangsan,female,22,zs@thoughtworks.com,10088888888]")))
                .andExpect(status().isOk());
    }
}
