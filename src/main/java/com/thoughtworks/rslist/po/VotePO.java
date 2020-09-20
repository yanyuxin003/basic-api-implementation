package com.thoughtworks.rslist.po;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "vote")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotePO {
    @Id
    @GeneratedValue
    private int id;
    private int voteNum;
    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPO user;

}