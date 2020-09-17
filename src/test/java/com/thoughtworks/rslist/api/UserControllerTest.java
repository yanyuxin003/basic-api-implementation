package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    public void  should_register_user() throws Exception{
        User user = new User("xiaowang","female",19,"a@thoughtworks.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString  = objectMapper.writeValueAsString(user);

        //然后向content传值 类型为MediaType.APPLICATION_JSON
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("xiaowang")))
                .andExpect(jsonPath("$[0].gender",is("female")))
                .andExpect(jsonPath("$[0].age",is(19)))
                .andExpect(jsonPath("$[0].email",is("a@thoughtworks.com")))
                .andExpect(jsonPath("$[0].phone",is("18888888888")))
                .andExpect(status().isOk());
    }


//    @Test
//    @Order(2)
//    void should_get_rs_event_list() throws Exception{
//        mockMvc.perform(get("/user"))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name",is("xiaowang")))
//                .andExpect(jsonPath("$[0].gender",is("female")))
//                .andExpect(jsonPath("$[0].age",is(19)))
//                .andExpect(jsonPath("$[0].email",is("a@thoughtworks.com")))
//                .andExpect(jsonPath("$[0].phone",is("18888888888")))
//                .andExpect(status().isOk());
////                .andExpect(jsonPath("$[0].name",is("mali")))
////                .andExpect(jsonPath("$[0].gender",is("male")))
////                .andExpect(jsonPath("$[0].age",is(20)))
////                .andExpect(jsonPath("$[0].email",is("ab@thoughtworks.com")))
////                .andExpect(jsonPath("$[0].phone",is("10088888888")))
////                .andExpect(status().isOk());
//    }


    @Test
    @Order(3)
    //validation homework 4:测试需要对每个验证条件进行覆盖（共设计了8个）
    public void  name_should_less_than_8() throws Exception{
        User user = new User("xiaowangssss","female",19,"a@thoughtworks.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString  = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }



    @Test
    @Order(4)
    public void  gender_should_null() throws Exception{
        User user = new User("xiaowangssss"," ",19,"a@thoughtworks.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString  = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(5)
    public void  email_should_suit_format() throws Exception{
        User user = new User("xiaowangssss","female",19,"a@@thoughtworks.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString  = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(6)
    public void  age_should_less_than_18() throws Exception{
        User user = new User("xiaowangssss","female",17,"a@thoughtworks.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString  = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void  age_should_more_than_100() throws Exception{
        User user = new User("xiaowangssss","female",118,"a@thoughtworks.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString  = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void  phone_should_not_with_1() throws Exception{
        User user = new User("xiaowangssss","female",17,"a@thoughtworks.com","28888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString  = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
