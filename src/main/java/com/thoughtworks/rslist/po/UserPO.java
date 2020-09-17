package com.thoughtworks.rslist.po;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class UserPO {
    @Id
    @GeneratedValue
    private int id;
//    @Column(name ="name")
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNum = 10;


}
