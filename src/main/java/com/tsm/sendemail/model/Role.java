
package com.tsm.sendemail.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import lombok.Getter;

@Entity
@Table(name = "role")
public class Role extends BaseModel{

	private Role() {
	}

	public enum Roles {
		ADMIN, USER;
	}

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Integer id;

	// @Column(name = "role")
	// private String role;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private Roles role;

	public void setId(final Integer id) {
		Assert.notNull(id, "The id must not be null!");
		this.id = id;
	}

	public void setRole(final Roles role) {
		Assert.notNull(role, "The role must not be null!");
		this.role = role;
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
		Role other = (Role) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	public static class RoleBuilder {

		private final Role role;

		private RoleBuilder(final Roles pRole) {
			role = new Role();
			role.setRole(pRole);
		}

		public static RoleBuilder Role(final Roles role) {
			return new RoleBuilder(role);
		}

		public Role build() {
			return role;
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
