package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.rslist.domain.UserList.userList;
@RestController
public class RsController {

    // private List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");
    private List<RsEvent> rsList = initRsEventList();

    private List<RsEvent> initRsEventList() {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        userList.add(user);
        List<RsEvent> rsEvents = new ArrayList<>();
        rsEvents.add(new RsEvent("第一条事件", "无关键字", user));
        rsEvents.add(new RsEvent("第二条事件", "无关键字", user));
        rsEvents.add(new RsEvent("第三条事件", "无关键字", user));
        return rsEvents;
    }

    @GetMapping("/rs/{index}")
    //customize-response homework 1: 将所有接口的返回值都替换成使用ResponseEntity
    public ResponseEntity getOneIndexEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.get(index - 1));
    }


    @GetMapping("/rs/list")
    public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        } else {
            return ResponseEntity.ok(rsList.subList(start - 1, end));
        }
    }


    @PostMapping("/rs/event")
    //customize-response homework 2: 所有post请求都返回201,并且返回的头部带上index字段（值为创建的资源在列表中的位置：eg: 添加的热搜事件在列表中的index）
    public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        if(!userList.contains(rsEvent.getUser())){
            userList.add(rsEvent.getUser());
            rsList.add(rsEvent);
            System.out.println(userList);
        }else {
            RsEvent rsEvent1 =  new RsEvent();
            rsEvent1.setEventName(rsEvent.getEventName());
            rsEvent1.setKeyWord(rsEvent.getKeyWord());
            rsEvent1.setUser(userList.get(1));
            rsList.add(rsEvent1);
        }
        //返回201，并且返回的头部带上index字段
        String index = String.valueOf(this.rsList.indexOf(rsEvent));
        return ResponseEntity.created(null).header("index",index).build();
    }


    @PatchMapping("/rs/event/{index}")
    public ResponseEntity updateOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        RsEvent event =rsList.get(index-1);

        if (rsEvent.getKeyWord() != null) {
            event.setKeyWord(rsEvent.getKeyWord());
        }
        if (rsEvent.getEventName() != null) {
            event.setEventName(rsEvent.getEventName());

        }
        if (rsEvent.getUser() != null) {
            event.setUser(rsEvent.getUser());
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
}

