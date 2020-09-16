package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.NewRsEvent;
import domain.NewRsEvent;
import domain.RsEvent;
import domain.User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class RsUserController {
     List<User> userList = initUserList();

    private List<User> initUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("xiaoli","female",19,"a@thoughtworks.com","18888888888"));
        userList.add(new User("xiaowang","male",16,"aB@thoughtworks.com","18888889999"));
        userList.add(new User("zhangsan","female",22,"zs@thoughtworks.com","10088888888"));
        return userList;
    }


    private  List<NewRsEvent> newRsEventList =  initNewRsEventList(userList);

    private List<NewRsEvent> initNewRsEventList(List<User>  userList) {
        List<NewRsEvent> rsEventsAndUser = new ArrayList<>();
        rsEventsAndUser.add(new NewRsEvent("第一条事件", "无关键字", userList.get(0)));
        rsEventsAndUser.add(new NewRsEvent("第二条事件", "无关键字", userList.get(1)));
        rsEventsAndUser.add(new NewRsEvent("第一条事件", "无关键字", userList.get(2)));
        return rsEventsAndUser;
    }

    @DirtiesContext
    @GetMapping("/newrs/{index}")
    public NewRsEvent getNewOneIndexEvent(@PathVariable int index) {
        return newRsEventList.get(index - 1);
    }

//    // 如果userName不存在，则将User添加到热搜事件列表中（相当于注册用户）
//    @PostMapping("/rsanduser/{index}")
//    public void addRsEventToNewRsEvent(@RequestBody String rsEvent){
//        ObjectMapper objectMapper = new ObjectMapper();
//
//    }
//
//    // 如果userName已存在在user列表中的话则只需添加热搜事件到热搜事件列表，
//    @PostMapping("/rsanduser/{index}")
//    public void addUserToNewRsEvent(@RequestBody @Valid User user){
//        NewRsEventList.add(user);
//    }


    @GetMapping("/rsanduser/list")
    public List<NewRsEvent> getNewRsEventList(){
        return  newRsEventList;
    }
}
