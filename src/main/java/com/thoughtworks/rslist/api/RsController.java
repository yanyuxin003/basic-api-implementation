package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class RsController {

    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/rs/{index}")
    public ResponseEntity getOneIndexEvent(@PathVariable int index) {
        Optional<RsEventPO> rsEventPO = rsEventRepository.findById(index);
        if (!rsEventPO.isPresent()) {
            throw new RsEventNotValidException("invalid rsEventId");
        }
        return ResponseEntity.ok(rsEventPO.get());
    }

    @GetMapping("/rsEvent")
    public ResponseEntity getRsEventList() {
        return ResponseEntity.ok(rsEventRepository.findAll());
    }


    @GetMapping("/users")
    public ResponseEntity get_all_user() {
        return ResponseEntity.ok(userRepository.findAll());
    }


    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());
        if (!userPO.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        RsEventPO rsEventPO = RsEventPO.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName())
                .userPO(userPO.get()).build();
        rsEventRepository.save(rsEventPO);
        return ResponseEntity.created(null).build();
    }


    @PatchMapping("/rs/{rsEventId}")
    public ResponseEntity should_update_rsEvent(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {
        RsEventPO rsEventPO = rsEventRepository.findById(rsEventId).get();
        if (rsEventPO.getUserPO().getId() == rsEvent.getUserId()) {
            if (rsEvent.getEventName() != null) {
                rsEventPO.setEventName(rsEvent.getEventName());
            }
            if (rsEvent.getKeyWord() != null) {
                rsEventPO.setKeyWord(rsEvent.getKeyWord());
            }
            rsEventRepository.save(rsEventPO);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

