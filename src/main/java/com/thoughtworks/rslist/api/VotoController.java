package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VotoController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> findAllByUserIdAndRsEventId(@RequestParam int userId, @RequestParam int rsEventId,@RequestParam int pageIndex) {
        PageRequest pageable = PageRequest.of(pageIndex-1,5);

        return ResponseEntity.ok(
                voteRepository.findAccordingToUserAndRsEvent(userId, rsEventId, pageable).stream()
                        .map(item -> Vote.builder().rsEventId(item.getRsEvent().getId())
                                .userId(item.getUser().getId())
                                .localDateTime(item.getLocalDateTime())
                                .voteNum(item.getVoteNum()).build()).collect(Collectors.toList())
        );
    }
}
