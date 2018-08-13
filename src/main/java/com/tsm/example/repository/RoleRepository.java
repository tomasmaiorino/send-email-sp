package com.tsm.example.repository;


import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.example.model.Role;


@Transactional(propagation = Propagation.MANDATORY)
public interface RoleRepository extends Repository<Role, Integer>, IBaseRepository<Role, Integer> {

    Role findByRole(String role);

}
