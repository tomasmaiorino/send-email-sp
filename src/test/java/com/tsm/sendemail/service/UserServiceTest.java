package com.tsm.sendemail.service;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.User;
import com.tsm.sendemail.repository.UserRepository;
import com.tsm.sendemail.util.UserTestBuilder;
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
	private UserRepository mockRepository;

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
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void save_DuplicatedEmailGiven_ShouldThrowException() {
		// Set up
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		// Do test
		try {
			service.save(user);
			fail();
		} catch (BadRequestException e) {
		}

		// Assertions
		verify(mockRepository).findByEmail(user.getEmail());
		verify(mockRepository, times(0)).save(user);
	}

	@Test
	public void save_ValidUserGiven_ShouldCreateUser() {
		// Set up
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(mockRepository.save(user)).thenReturn(user);
		when(mockRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

		// Do test
		User result = service.save(user);

		// Assertions
		verify(mockRepository).save(user);
		verify(mockRepository).findByEmail(user.getEmail());
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
		verifyZeroInteractions(mockRepository);
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
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void findByEmail_UserNotFound_ShouldThrowException() {
		// Set up
		String email = UserTestBuilder.getValidEmail();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Do test
		try {
			service.findByEmail(email);
			fail();
		} catch (ResourceNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findByEmail(email);
	}

	@Test
	public void findByEmail_UserFound_ShouldReturnUser() {
		// Set up
		String email = UserTestBuilder.getValidEmail();
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.of(user));

		// Do test
		User result = service.findByEmail(email);

		// Assertions
		verify(mockRepository).findByEmail(email);

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
		verifyZeroInteractions(mockRepository);
	}

	@Test
	public void loadUserByUsername_UserNotFound_ShouldThrowException() {
		// Set up
		String email = UserTestBuilder.getValidEmail();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Do test
		try {
			service.loadUserByUsername(email);
			fail();
		} catch (UsernameNotFoundException e) {
		}

		// Assertions
		verify(mockRepository).findByEmail(email);
	}

	@Test
	public void loadUserByUsername_UserFound_ShouldReturnUser() {
		// Set up
		String email = UserTestBuilder.getValidEmail();
		User user = UserTestBuilder.buildModel();

		// Expectations
		when(mockRepository.findByEmail(email)).thenReturn(Optional.of(user));

		// Do test
		UserDetails result = service.loadUserByUsername(email);

		// Assertions
		verify(mockRepository).findByEmail(email);

		assertNotNull(result);
		assertThat(result, allOf(hasProperty("username", is(user.getEmail())),
				hasProperty("password", is(user.getPassword()))));
	}
}
