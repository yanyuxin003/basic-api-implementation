package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();


    @PostMapping("/user")
    public void addUser(@Valid @RequestBody User user) {
        userList.add(user);
    }

    @GetMapping("/user/list")
    public List<User> getUserList() {
        return userList;
    }
}
