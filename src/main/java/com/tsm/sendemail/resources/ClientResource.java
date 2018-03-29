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

import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;

public class ClientResource implements BaseResource {

	@Getter
	private Integer id;

	@Getter
	@NotNull(message = REQUIRED_NAME)
	@Size(min = 2, max = 30, message = INVALID_NAME_SIZE)
	private String name;

	@Getter
	@NotEmpty(message = REQUIRED_EMAIL)
	@Email(message = INVALID_EMAIL)
	private String email;

	@Getter
	@NotNull(message = REQUIRED_TOKEN)
	@Size(min = 2, max = 50, message = INVALID_TOKEN_SIZE)
	private String token;

	@Getter
	@Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_STATUS)
	private String status;

	@Getter
	@NotEmpty(message = REQUIRED_HOSTS)
	private Set<String> hosts;

	@Getter
	@NotEmpty(message = REQUIRED_EMAIL_RECIPIENT)
	@Email(message = INVALID_EMAIL)
	private String emailRecipient;

	@Getter
	private Map<String, String> attributes;

	@Getter
	private Boolean isAdmin = false;

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public void setHosts(final Set<String> hosts) {
		this.hosts = hosts;
	}

	public void setEmailRecipient(final String emailRecipient) {
		this.emailRecipient = emailRecipient;
	}

	public void setAttributes(final Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setIsAdmin(final Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ClientResource other = (ClientResource) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
