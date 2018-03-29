package com.tsm.sendemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.tsm.sendemail.model.Role;
import com.tsm.sendemail.model.User;
import com.tsm.sendemail.model.User.UserBuilder;
import com.tsm.sendemail.model.User.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile(value = "it")
@Slf4j
public class InitialItlLoad implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private UserService userService;

	@Value("${user.it.email}")
	@Getter
	@Setter
	private String userItEmail;

	@Value("${user.it.name}")
	@Getter
	@Setter
	private String userItName;

	@Value("${user.it.pass}")
	@Getter
	@Setter
	private String userItPass;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		log.info("Creating integration user.");
		UserBuilder builder = User.UserBuilder.User(userItName, userItEmail, bCryptPasswordEncoder.encode(userItPass),
				UserStatus.ACTIVE);
		builder.role(Role.RoleBuilder.Role(Role.Roles.USER).build());
		User user = builder.build();
		log.info("Saving integration user [{}].", user);
		userService.save(user);

	}

}
