package com.tsm.example.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import lombok.Getter;

@Entity
@Table(name = "send_user")
public class User extends BaseModel {

	private User() {

	}

	@Id
	@GeneratedValue
	@Getter
	@Column(name = "user_id")
	private Integer id;

	public enum UserStatus {
		ACTIVE, INACTIVE;
	}

	@Getter
	@Column(nullable = false, length = 30)
	private String name;

	@Getter
	@Column(nullable = false, length = 30)
	private String email;

	@Getter
	@Column(nullable = false)
	private String password;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName="user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName="role_id"))
	private Set<Role> roles;

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(final Set<Role> roles) {
		Assert.notEmpty(roles, "The roles must not be null or empty!");

		if (!CollectionUtils.isEmpty(this.getRoles())) {
			this.getRoles().clear();
		} else {
			this.roles = new HashSet<>();
		}
		if (!CollectionUtils.isEmpty(roles)) {
			this.roles.addAll(roles);
		}

		this.roles = roles;

	}

	public void setPassword(final String password) {
		Assert.hasText(password, "The password must not be null or empty!");
		this.password = password;
	}

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null or empty!");
		this.name = name;
	}

	public void setEmail(final String email) {
		Assert.hasText(email, "The email must not be null or empty!");
		this.email = email;
	}

	public void setCustomerStatus(final UserStatus status) {
		Assert.notNull(status, "The status must not be null!");
		this.status = status;
	}

	public void addRole(final Role role) {
		Assert.notNull(role, "The role must not be null!");
		if (CollectionUtils.isEmpty(roles)) {
			roles = new HashSet<>();
		}
		roles.add(role);

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
		User other = (User) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	public static class UserBuilder {

		private final User user;

		private UserBuilder(final String name, final String email, final String password, final UserStatus status) {
			user = new User();
			user.setEmail(email);
			user.setName(name);
			user.setPassword(password);
			user.setCustomerStatus(status);

		}

		public static UserBuilder User(final String name, final String email, final String password,
				final UserStatus status) {
			return new UserBuilder(name, email, password, status);
		}

		public UserBuilder role(final Role role) {
			user.addRole(role);
			return this;
		}

		public User build() {
			return user;
		}
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
