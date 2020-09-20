package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;

import com.thoughtworks.rslist.exception.UserNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    RsEventRepository rsEventRepository;

    private List<UserPO> userList = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    UserRepository userRepository;

    @PostMapping("/user")
    public void addUser(@Valid @RequestBody User user) {
        UserPO userPO = new UserPO();
        userPO.setName(user.getName());
        userPO.setAge(user.getAge());
        userPO.setGender(user.getGender());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity should_delete_user_and_rsEvent(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/user/{index}")
    public ResponseEntity getOneIndexUser(@Valid @PathVariable int index) {
        Optional<UserPO> user = userRepository.findById(index);
        if (user.get() != null) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/user/list")
    public ResponseEntity getUserList() {
        userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}
