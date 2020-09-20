package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rsEvent")
public class RsEventPO {
    @Id
    @GeneratedValue
    private int id;
    private String eventName;

    private String keyWord;

    private int voteNum;
//    @ManyToOne
//    private UserPO userId;

    @ManyToOne
    @JsonIgnore
    private UserPO userPO;

}
