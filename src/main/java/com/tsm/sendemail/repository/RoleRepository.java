package com.tsm.sendemail.repository;


import com.tsm.sendemail.model.Role;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation = Propagation.MANDATORY)
public interface RoleRepository extends Repository<Role, Integer>, IBaseRepository<Role, Integer> {

    Role findByRole(String role);

}
