package com.thoughtworks.rslist.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {

   // @NotNull
    private String eventName;
   // @NotNull
    private String keyWord;
    @NotNull
    private int userId;
    private int voteNum = 0;

}