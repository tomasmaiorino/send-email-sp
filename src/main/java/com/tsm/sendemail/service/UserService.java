package com.tsm.sendemail.service;


import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.User;
import com.tsm.sendemail.repository.UserRepository;
import com.tsm.sendemail.repository.IBaseRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
@Slf4j

public class UserService extends BaseService<User, Integer> implements UserDetailsService {

	private static final String DUPLICATED_TOKEN = null;
	@Autowired
	private UserRepository repository;

	public IBaseRepository<User, Integer> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return User.class.getSimpleName();
	}

	@Override
	protected void saveValidation(final User model) {
		repository.findByEmail(model.getEmail()).ifPresent(c -> {
			throw new BadRequestException(DUPLICATED_TOKEN);
		});

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Assert.hasText(email, "The email must not be empty!");
		log.info("loadUserByUsername user by email [{}] .", email);

		User user = repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				Collections.emptyList());
	}

	private Optional<User> findOptionalByEmail(final String email) {
		log.info("Finding user by email [{}] .", email);
		Optional<User> optCustomer = repository.findByEmail(email);
		log.info("Model found [{}].", optCustomer.isPresent());
		return optCustomer;
	}

	public User findByEmail(final String email) {
		Assert.hasText(email, "The email must not be empty!");
		User user = findOptionalByEmail(email).orElseThrow(() -> new ResourceNotFoundException(""));
		return user;
	}

}
