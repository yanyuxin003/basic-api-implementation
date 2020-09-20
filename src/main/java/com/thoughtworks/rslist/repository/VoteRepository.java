package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VotePO, Integer> {
    @Override
    List<VotePO> findAll();

    //List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
    @Query("select v from VotePO v where v.user.id = :userId and v.rsEvent.id = :rsEventId")
    List<VotePO> findAccordingToUserAndRsEvent(int userId, int rsEventId, Pageable pageable);
}
