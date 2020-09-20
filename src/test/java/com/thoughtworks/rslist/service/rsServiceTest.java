package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

public class rsServiceTest {
    rsService rsService;
    @Mock
    RsEventRepository rsEventRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    VoteRepository voteRepository;
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        initMocks(this);
        rsService = new rsService(rsEventRepository, userRepository, voteRepository);
        localDateTime = LocalDateTime.now();
    }

    @Test
    public void should_vote_success() {
        Vote vote = Vote.builder().voteNum(2).localDateTime(localDateTime).userId(1).rsEventId(1).build();
        UserPO userPO = UserPO.builder().name("yyx").age(20).email("yyx@b.com").gender("male").phone("12345678910").voteNum(6).build();
        RsEventPO rsEventPO = RsEventPO.builder().userPO(userPO).eventName("eventName").keyWord("keyword").voteNum(1).id(1).build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPO));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPO));

        rsService.vote(vote, 1);
        verify(voteRepository).save(VotePO.builder().user(userPO).rsEvent(rsEventPO).localDateTime(localDateTime).voteNum(2).build());
        verify(rsEventRepository).save(rsEventPO);
        verify(userRepository).save(userPO);
        assertEquals(userPO.getVoteNum(), 4);
        assertEquals(rsEventPO.getVoteNum(), 3);
    }


    @Test
    public void should_throw_exception_when_rsEventId_not_exit() {
        Vote vote = Vote.builder().voteNum(2).localDateTime(localDateTime).userId(1).rsEventId(3).build();
        UserPO userPO = UserPO.builder().name("yyx").age(20).email("yyx@b.com").gender("male").phone("12345678910").voteNum(6).build();
        RsEventPO rsEventPO = RsEventPO.builder().userPO(userPO).eventName("猪肉涨价了").keyWord("经济").voteNum(1).id(1).build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPO));

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }

    @Test
    public void should_throw_exception_when_userId_not_exit() {
        Vote vote = Vote.builder().voteNum(2).localDateTime(localDateTime).userId(3).rsEventId(2).build();
        UserPO userPO = UserPO.builder().name("yyx").age(20).email("yyx@b.com").gender("male").phone("12345678910").voteNum(6).build();
        RsEventPO rsEventPo = RsEventPO.builder().userPO(userPO).eventName("猪肉涨价了").keyWord("经济").voteNum(1).id(1).build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPo));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }

    @Test
    public void should_throw_exception_when_voteNum_invalid() {
        Vote vote = Vote.builder().voteNum(6).localDateTime(localDateTime).userId(1).rsEventId(1).build();
        UserPO userPO = UserPO.builder().name("yyx").age(20).email("yyx@b.com").gender("male").phone("12345678910").voteNum(3).build();
        RsEventPO rsEventPO = RsEventPO.builder().userPO(userPO).eventName("猪肉涨价了").keyWord("经济").voteNum(1).id(1).build();
        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPO));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPO));

        when(rsEventRepository.findById(anyInt())).thenReturn(Optional.of(rsEventPO));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userPO));

        assertThrows(RuntimeException.class, () -> rsService.vote(vote, 1));
    }

}
