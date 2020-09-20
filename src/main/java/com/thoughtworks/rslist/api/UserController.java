package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;

import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

    // @Autowired
    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<UserPO> userList = new ArrayList<>();


    @PostMapping("/user")
    public ResponseEntity addUser(@Valid @RequestBody User user) {
        UserPO userPO = new UserPO();
        userPO.setName(user.getName());
        userPO.setAge(user.getAge());
        userPO.setGender(user.getGender());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
        return ResponseEntity.ok().build();

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
