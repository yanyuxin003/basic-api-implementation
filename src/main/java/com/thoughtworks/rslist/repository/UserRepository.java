package com.thoughtworks.rslist.repository;

import com.sun.xml.bind.v2.model.core.ID;
import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserPO, Integer> {
    @Override
    List<UserPO> findAll();
    UserPO findById(ID id);
}
