package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import java.util.Optional;

public class rsService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public rsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(Vote vote, int rsEventId) {
        Optional<RsEventPO> rsEventDto = rsEventRepository.findById(rsEventId);
        Optional<UserPO> userDto = userRepository.findById(vote.getUserId());
        if (!rsEventDto.isPresent() || !userDto.isPresent()
                || vote.getVoteNum() > userDto.get().getVoteNum()) {
            throw new RuntimeException();
        }
        VotePO votePO = VotePO.builder().user(userDto.get()).rsEvent(rsEventDto.get())
                        .localDateTime(vote.getLocalDateTime()).voteNum(vote.getVoteNum()).build();
        voteRepository.save(votePO);
        UserPO addUserPo = userDto.get();
        addUserPo.setVoteNum(addUserPo.getVoteNum() - vote.getVoteNum());
        userRepository.save(addUserPo);
        RsEventPO newRsEventPo = rsEventDto.get();
        newRsEventPo.setVoteNum(newRsEventPo.getVoteNum() + vote.getVoteNum());
        rsEventRepository.save(newRsEventPo);
    }
}