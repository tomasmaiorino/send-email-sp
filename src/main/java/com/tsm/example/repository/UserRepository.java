
package com.tsm.example.repository;


import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.example.model.User;

import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends Repository<User, Integer>, IBaseRepository<User, Integer> {

	Optional<User> findByEmail(final String email);

}
