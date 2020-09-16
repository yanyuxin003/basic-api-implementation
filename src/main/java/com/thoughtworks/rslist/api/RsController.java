package com.thoughtworks.rslist.api;


import domain.RsEvent;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

    // private List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");
    private List<RsEvent> rsList = initRsEventList();

    private List<RsEvent> initRsEventList() {
        List<RsEvent> rsEvents = new ArrayList<>();
        rsEvents.add(new RsEvent("第一条事件", "无关键字"));
        rsEvents.add(new RsEvent("第二条事件", "无关键字"));
        rsEvents.add(new RsEvent("第三条事件", "无关键字"));
        return rsEvents;
    }
//          Arrays.asList("第一条事件","第二条事件","第三条事件");

    @DirtiesContext
    @GetMapping("/rs/{index}")
    public RsEvent getOneIndexEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @DirtiesContext
    @GetMapping("/rs/list")
    public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        } else {
            return rsList.subList(start - 1, end);
        }
    }

    @DirtiesContext
    @PostMapping("/rs/event")
    public void addEvent(@RequestBody String rsEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent1 = objectMapper.readValue(rsEvent, RsEvent.class);
        rsList.add(rsEvent1);
    }

    @DirtiesContext
    @PatchMapping("/rs/event/{index}")
    public void updateOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        if (rsEvent.getEventName() != null) {//怎么判断空 多个属性！遗留问题！index判断！
            rsList.get(index - 1).setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
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

