package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VotoController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> should_get_record(@RequestParam int userId, @RequestParam int rsEventId) {
        return ResponseEntity.ok(
                voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId).stream()
                        .map(item -> Vote.builder().rsEventId(item.getRsEvent().getId()).userId(item.getUser().getId())
                                .localDateTime(item.getLocalDateTime()).voteNum(item.getVoteNum()).build()).collect(Collectors.toList())
        );
    }
}
