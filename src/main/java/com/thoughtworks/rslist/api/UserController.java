package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user")
    public void addUser(@Valid @RequestBody User user) {
        userList.add(user);
    }

    @GetMapping("/user")
    public List<User> getUserList() {
        return userList;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Error> exceptionHandler(MethodArgumentNotValidException e) {
        logger.error("Here is a invalid user");
        return ResponseEntity.badRequest().body(new Error("invalid user"));
    }
}
