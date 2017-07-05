package com.tsm.sendemail.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import lombok.Getter;

@Entity
@Table(name = "client_hosts")
public class ClientHosts extends BaseModel {

    @Id
    @GeneratedValue
    @Getter
    private Integer id;

    @Getter
    @Column(nullable = false, length = 100)
    private String host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public void setClient(final Client client) {
        Assert.notNull(client, "The client must not be null!");
        this.client = client;
    }

    public void setHost(final String host) {
        Assert.hasText(host, "The host must not be null!");
        this.host = host;
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
        ClientHosts other = (ClientHosts) obj;
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
