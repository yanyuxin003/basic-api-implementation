package com.thoughtworks.rslist.api;


import domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class UserController {
    List<User> userList = new ArrayList<>();

    //notice: 注意@Valid和@Validated的配合使用
    @PostMapping("/user")
    public void addUser(@RequestBody @Valid User user){
        userList.add(user);
    }

    @GetMapping("/user")
    public List<User> getUserList(){
        return  userList;
    }
}
