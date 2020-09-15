package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsEventList();

    private List<RsEvent> initRsEventList() {
        List<RsEvent> rsEvents = new ArrayList<>(); 
        rsEvents.add(new RsEvent("第一条事件", "无关键字"));
        rsEvents.add(new RsEvent("第二条事件", "无关键字"));
        rsEvents.add(new RsEvent("第三条事件", "无关键字"));
        rsEvents.add(new RsEvent("猪肉涨价了", "食品"));
        return rsEvents;
    }
//          Arrays.asList("第一条事件","第二条事件","第三条事件");


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
    public void addEvent(@RequestBody String jsonSting) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent1 = objectMapper.readValue(jsonSting, RsEvent.class);
        rsList.add(rsEvent1);
    }
}