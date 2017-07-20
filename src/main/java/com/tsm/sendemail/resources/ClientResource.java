package com.tsm.sendemail.resources;

import static com.tsm.sendemail.util.ErrorCodes.INVALID_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_NAME_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_STATUS;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_TOKEN_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_EMAIL_RECIPIENT;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_HOSTS;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_NAME;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_TOKEN;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

public class ClientResource extends BaseResource {

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_NAME)
	@Size(min = 2, max = 30, message = INVALID_NAME_SIZE)
	private String name;

	@Getter
	@Setter
	@NotEmpty(message = REQUIRED_EMAIL)
	@Email(message = INVALID_EMAIL)
	private String email;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_TOKEN)
	@Size(min = 2, max = 50, message = INVALID_TOKEN_SIZE)
	private String token;

	@Getter
	@Setter
	@Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_STATUS)
	private String status;

	@Getter
	@Setter
	@NotEmpty(message = REQUIRED_HOSTS)
	private Set<String> hosts;

	@Getter
	@Setter
	@NotEmpty(message = REQUIRED_EMAIL_RECIPIENT)
	@Email(message = INVALID_EMAIL)
	private String emailRecipient;

	@Getter
	@Setter
	private Boolean isAdmin = false;

}
