package com.tsm.sendemail.resources;

import static com.tsm.sendemail.util.ErrorCodes.FIELD_REQUIRED;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_NAME_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_TOKEN_SIZE;

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
	@NotNull(message = FIELD_REQUIRED)
	@Size(min = 2, max = 30, message = INVALID_NAME_SIZE)
	private String name;

	@Getter
	@Setter
	// @NotNull(message = FIELD_REQUIRED)
	@NotEmpty(message = FIELD_REQUIRED)
	@Email(message = INVALID_EMAIL)
	private String email;

	@Getter
	@Setter
	@NotNull(message = FIELD_REQUIRED)
	@Size(min = 2, max = 30, message = INVALID_TOKEN_SIZE)
	private String token;

	@Getter
	@Setter
	@Pattern(regexp = "", message = INVALID_TOKEN_SIZE)
	private String status;

	@Getter
	@Setter
	@NotEmpty(message = FIELD_REQUIRED)
	private Set<String> hosts;

}
