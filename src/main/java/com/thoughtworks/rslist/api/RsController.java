package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
    //validati homework 1：重构接口及测试
    private List<RsEvent> initRsEventList() {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        userList.add(user);
        List<RsEvent> rsEvents = new ArrayList<>();
        rsEvents.add(new RsEvent("第一条事件", "无关键字", user));
        rsEvents.add(new RsEvent("第二条事件", "无关键字", user));
        rsEvents.add(new RsEvent("第三条事件", "无关键字", user));
        return rsEvents;
    }
//      Arrays.asList("第一条事件","第二条事件","第三条事件");

    @GetMapping("/rs/{index}")
    public RsEvent getOneIndexEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }


    @GetMapping("/rs/list")
    public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        } else {
            return rsList.subList(start - 1, end);
        }
    }


    @PostMapping("/rs/event")
    //validation homework 3 : 如果userName已存在在user列表中的话则只需添加热搜事件到热搜事件列表，如果userName不存在，则将User添加到热搜事件列表中（相当于注册用户）
    public void addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        RsEvent rsEvent1 = objectMapper.readValue(rsEvent, RsEvent.class);
//        rsList.add(rsEvent1);
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
    }


    @PatchMapping("/rs/event/{index}")
    public void updateOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        if (rsEvent.getEventName() != null) {
            rsList.get(index - 1).setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
        }
        if (rsEvent.getEventName().length()!=0 && rsEvent.getKeyWord().length()!=0) {
            rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
            rsList.get(index - 1).setEventName(rsEvent.getEventName());
        }

    }


    @DeleteMapping("/rs/event/{index}")
    public void deleteOneRsEvent(@PathVariable int index) {
        if (index < 1 || index > rsList.size()) {//
            return;//throw 异常
        }
        rsList.remove(index - 1);
    }
}

