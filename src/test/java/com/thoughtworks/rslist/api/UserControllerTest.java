package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @Test
    @Order(1)
    public void should_register_user() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        //然后向content传值 类型为MediaType.APPLICATION_JSON
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //提取变量 userRepository.findAll();快捷键？
        List<UserPO> all = userRepository.findAll();
        assertEquals(1, all.size());
        assertEquals("xiaowang", all.get(0).getName());
        assertEquals(19, all.get(0).getAge());
    }

    @Test
    @Order(2)
    void should_get_one_user_by_id() throws Exception {
//        User user = new User("xiaowang","female",19,"a@thoughtworks.com","18888888888");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString  = objectMapper.writeValueAsString(user);
//        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        List<UserPO> all = userRepository.findAll();

        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("xiaowang")))
                .andExpect(jsonPath("$.gender", is("female")))
                .andExpect(jsonPath("$.age", is(19)))
                .andExpect(jsonPath("$.email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$.phone", is("18888888888")))
                .andExpect(jsonPath("$.voteNum", is(10)))
                .andExpect(status().isOk());
    }


    @Test
    @Order(3)
    //validation homework 4:测试需要对每个验证条件进行覆盖（共设计了8个）
    public void name_should_less_than_8() throws Exception {
        User user = new User("xiaowangssss", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


    @Test
    @Order(4)
    public void gender_should_null() throws Exception {
        User user = new User("xiaowang", null, 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(5)
    public void email_should_suit_format() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(6)
    public void age_should_less_than_18() throws Exception {
        User user = new User("xiaowang", "female", 17, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(7)
    public void age_should_more_than_100() throws Exception {
        User user = new User("xiaowangssss", "female", 118, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(8)
    public void phone_should_not_with_1() throws Exception {
        User user = new User("xiaowangssss", "female", 17, "a@thoughtworks.com", "28888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


    @Test
    @Order(9)
    void should_delete_one_user_by_id() throws Exception {
        mockMvc.perform(delete("/user/1")).andExpect(status().isOk());
        List<UserPO> all = userRepository.findAll();
        assertEquals(1, all.size());
        assertEquals("xiaowang", all.get(0).getName());
        assertEquals(19, all.get(0).getAge());
    }


    @Test
    public void should_delete_User() throws Exception {
        UserPO userPO = UserPO.builder().name("xiaowang").age(19).
                gender("female").email("a@thoughtworks.com").phone("18888888888").voteNum(10).build();
        userRepository.save(userPO);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("涨工资了").userPO(userPO).build();
        rsEventRepository.save(rsEventPO);
        mockMvc.perform(delete("/user/{id}", userPO.getId())).andExpect(status().isOk());
        assertEquals(0, userRepository.findAll().size());
        assertEquals(0, rsEventRepository.findAll().size());
    }
}
