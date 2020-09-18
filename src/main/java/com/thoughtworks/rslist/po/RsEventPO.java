package com.thoughtworks.rslist.po;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    private int userId;
}
