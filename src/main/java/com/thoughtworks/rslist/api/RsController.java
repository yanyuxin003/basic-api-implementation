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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.thoughtworks.rslist.domain.UserList.userList;

@RestController
public class RsController {

    // private List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");
    private List<RsEvent> rsList = initRsEventList();

    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;

    private List<RsEvent> initRsEventList() {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        userList.add(user);
        List<RsEvent> rsEvents = new ArrayList<>();
        rsEvents.add(new RsEvent("第一条事件", "无关键字", 1));
        rsEvents.add(new RsEvent("第二条事件", "无关键字", 1));
        rsEvents.add(new RsEvent("第三条事件", "无关键字", 1));
        return rsEvents;
    }

    @GetMapping("/rs/{index}")
    //customize-response homework 1: 将所有接口的返回值都替换成使用ResponseEntity
    public ResponseEntity getOneIndexEvent(@PathVariable int index) {
        if (index <= 0 || index > rsList.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        return ResponseEntity.ok(rsList.get(index - 1));
    }


    @GetMapping("/rs/list")
    public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start != null || end != null) {
            if (start < 0 || end > rsList.size()) {
                throw new RsEventNotValidException("invalid request param");
            } else if (start != null && start < 0) {
                throw new RsEventNotValidException("invalid request param");
            } else if (end != null && end > rsList.size()) {
                throw new RsEventNotValidException("invalid request param");
            }
            return ResponseEntity.ok(rsList.subList(start - 1, end));
        }
        return ResponseEntity.ok(rsList);
    }


    @PatchMapping("/rs/event/{index}")
    public ResponseEntity updateOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        RsEvent event = rsList.get(index - 1);

        if (rsEvent.getKeyWord() != null) {
            event.setKeyWord(rsEvent.getKeyWord());
        }
        if (rsEvent.getEventName() != null) {
            event.setEventName(rsEvent.getEventName());

        }
        if (rsEvent.getUserId() != 0) {
            event.setUserId(rsEvent.getUserId());
        }
        return ResponseEntity.ok(null);

    }


    @DeleteMapping("/rs/event/{index}")
    public ResponseEntity deleteOneRsEvent(@PathVariable int index) {
        if (index < 1 || index > rsList.size()) {//
            return (ResponseEntity) ResponseEntity.badRequest();//throw 异常
        }
        rsList.remove(index - 1);
        return ResponseEntity.ok(null);
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

