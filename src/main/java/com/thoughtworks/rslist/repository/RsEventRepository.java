package com.thoughtworks.rslist.repository;

import com.sun.xml.bind.v2.model.core.ID;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventPO, Integer> {
    @Override
    List<RsEventPO> findAll();

}
