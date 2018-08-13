package com.tsm.example.service;

import com.tsm.example.exceptions.BadRequestException;
import com.tsm.example.exceptions.ResourceNotFoundException;
import com.tsm.example.model.User;
import com.tsm.example.repository.UserRepository;
import com.tsm.example.service.UserService;
import com.tsm.example.util.UserTestBuilder;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class UserServiceTest {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save_NullUserGiven_ShouldThrowException() {
		// Set up
		User user = null;

		// Do test
		try {
			service.save(user);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void save_DuplicatedEmailGiven_ShouldThrowException() {
		// Set up
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		// Do test
		try {
			service.save(user);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(repository).findByEmail(user.getEmail());
		verify(repository, times(0)).save(user);
	}

	@Test
	public void save_ValidUserGiven_ShouldCreateUser() {
		// Set up
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(repository.save(user)).thenReturn(user);
		when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

		// Do test
		User result = service.save(user);

		// Assertions
		verify(repository).save(user);
		verify(repository).findByEmail(user.getEmail());
		assertNotNull(result);
		assertThat(result, is(user));
	}

	@Test
	public void findByEmail_NullUserEmailGiven_ShouldThrowException() {
		// Set up
		String token = null;

		// Do test
		try {
			service.findByEmail(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void findByEmail_EmptyUserEmailGiven_ShouldThrowException() {
		// Set up
		String token = "";

		// Do test
		try {
			service.findByEmail(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void findByEmail_UserNotFound_ShouldThrowException() {
		// Set up
		String email = UserTestBuilder.getValidEmail();

		// Expectations
		when(repository.findByEmail(email)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findByEmail(email);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(repository).findByEmail(email);
	}

	@Test
	public void findByEmail_UserFound_ShouldReturnUser() {
		// Set up
		String email = UserTestBuilder.getValidEmail();
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(repository.findByEmail(email)).thenReturn(Optional.of(user));

		// Do test
		User result = service.findByEmail(email);

		// Assertions
		verify(repository).findByEmail(email);

		assertNotNull(result);
		assertThat(result, is(user));
	}

	//
	@Test
	public void loadUserByUsername_EmptyUserEmailGiven_ShouldThrowException() {
		// Set up
		String token = "";

		// Do test
		try {
			service.loadUserByUsername(token);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// Assertions
		verifyZeroInteractions(repository);
	}

	@Test
	public void loadUserByUsername_UserNotFound_ShouldThrowException() {
		// Set up
		String email = UserTestBuilder.getValidEmail();

		// Expectations
		when(repository.findByEmail(email)).thenReturn(Optional.empty());

		// Do test
		try {
			service.loadUserByUsername(email);
			fail();
		} catch (UsernameNotFoundException e) {
		}

		// Assertions
		verify(repository).findByEmail(email);
	}

	@Test
	public void loadUserByUsername_UserFound_ShouldReturnUser() {
		// Set up
		String email = UserTestBuilder.getValidEmail();
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(repository.findByEmail(email)).thenReturn(Optional.of(user));

		// Do test
		UserDetails result = service.loadUserByUsername(email);

		// Assertions
		verify(repository).findByEmail(email);

		assertNotNull(result);
		assertThat(result, allOf(hasProperty("username", is(user.getEmail())),
				hasProperty("password", is(user.getPassword()))));
	}
}
