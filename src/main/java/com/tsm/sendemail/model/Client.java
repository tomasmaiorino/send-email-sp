package com.tsm.sendemail.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.util.Assert;

import lombok.Getter;

@Entity
@Table(name = "client")
public class Client extends BaseModel {

    public enum ClientStatus {
        ACTIVE, INACTIVE;
    }

    @JoinColumn(name = "client_hosts")
    @ManyToOne
    @Getter
    private Set<ClientHosts> clientHosts;

    @Getter
    private String name;

    @Getter
    private String token;

    @Getter
    private String email;

    @Getter
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClientStatus status;

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

    public void setToken(final String token) {
        Assert.hasText(token, "The token must not be null or empty!");
        this.token = token;
    }

    public void setClientStatus(final ClientStatus status) {
        Assert.notNull(status, "The status must not be null!");
        this.status = status;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }
}
