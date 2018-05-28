package com.tsm.sendemail.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "client")
public class Client extends BaseModel {

	public Client() {

	}

	@Id
	@GeneratedValue
	@Getter
	private Integer id;

	public enum ClientStatus {
		ACTIVE, INACTIVE;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "client")
	private Set<ClientHosts> clientHosts;

	@Getter
	@Column(nullable = false, length = 30)
	private String name;

	@Getter
	@Column(nullable = false, length = 30)
	private String token;

	@Getter
	@Column(nullable = false, length = 30)
	private String email;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private ClientStatus status;

	@Getter
	@Column(nullable = false, length = 30)
	private String emailRecipient;

	@Column(name = "is_admin")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Getter
	@Setter
	public Boolean isAdmin = false;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
	@JsonBackReference
	private Set<ClientAttribute> clientAttributes;

	public void setClientHosts(final Set<ClientHosts> clientHosts) {
		Assert.notNull(clientHosts, "The clientHosts must not be null!");
		Assert.isTrue(!clientHosts.isEmpty(), "The clientHosts must not be empty!");
		this.clientHosts = clientHosts;
	}

	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null or empty!");
		this.name = name;
	}

	public void setEmail(final String email) {
		Assert.hasText(email, "The email must not be null or empty!");
		this.email = email;
	}

	public void setEmailRecipient(final String emailRecipient) {
		Assert.hasText(emailRecipient, "The emailRecipient must not be null or empty!");
		this.emailRecipient = emailRecipient;
	}

	public void setToken(final String token) {
		Assert.hasText(token, "The token must not be null or empty!");
		this.token = token;
	}

	public void setClientStatus(final ClientStatus status) {
		Assert.notNull(status, "The status must not be null!");
		this.status = status;
	}

	public Set<ClientHosts> getClientHosts() {
		return clientHosts;
	}

	public Set<ClientAttribute> getClientAttributes() {
		return clientAttributes;
	}

	public void addAttribute(final ClientAttribute attribute) {
		Assert.notNull(attribute, "The attribute must not be null!");
		if (CollectionUtils.isEmpty(clientAttributes)) {
			clientAttributes = new HashSet<>();
		}
		clientAttributes.add(attribute);

	}

	public void setClientAttribute(Set<ClientAttribute> clientAttributes) {
		Assert.notEmpty(clientAttributes, "The clientAttributes must not be null or empty!");
		if (!CollectionUtils.isEmpty(this.getClientAttributes())) {
			this.getClientAttributes().clear();
		} else {
			this.clientAttributes = new HashSet<>();
		}
		if (!CollectionUtils.isEmpty(clientAttributes)) {
			this.clientAttributes.addAll(clientAttributes);
		}

		this.clientAttributes = clientAttributes;
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
		Client other = (Client) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	public String getClientAttributeValueByKey(final String key) {
		Assert.hasText(key, "The key must not be null or empty!");
		if (CollectionUtils.isEmpty(getClientAttributes())) {
			return null;
		}
		Optional<ClientAttribute> optClient = getClientAttributes().stream().filter(ca -> key.equals(ca.getKey()))
				.findFirst();

		if (optClient.isPresent()) {
			return optClient.get().getValue();
		}
		return null;
	}

	public static class ClientBuilder {

		private final Client client;

		private ClientBuilder(final String name, final String email, final String token, final ClientStatus status,
				final String emailRecipient, final Boolean isAdmin) {
			client = new Client();
			client.setClientStatus(status);
			client.setEmail(email);
			client.setToken(token);
			client.setName(name);
			client.setIsAdmin(isAdmin);
			client.setEmailRecipient(emailRecipient);

		}

		public static ClientBuilder Client(final String name, final String email, final String token,
				final ClientStatus status, final String emailRecipient, final Boolean isAdmin) {
			return new ClientBuilder(name, email, token, status, emailRecipient, isAdmin);
		}

		public ClientBuilder clientHosts(final Set<ClientHosts> clientHosts) {
			client.setClientHosts(clientHosts);
			return this;
		}

		public Client build() {
			return client;
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
