package com.tsm.sendemail.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

@Entity
@Table(name = "client_attribute")
public class ClientAttribute extends BaseModel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "key_name", nullable = false)
    private String key;

    @Column(name = "key_value", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        Assert.hasText(key, "The key must not be null or empty!");
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        Assert.hasText(value, "The value must not be null or empty!");
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client client) {
        Assert.notNull(client, "The client must not be null!");
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
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
        ClientAttribute other = (ClientAttribute) obj;
        if (client == null || other.getClient() == null ||
            key == null || other.key == null) {
            return false;
        }

        return client.equals(other.getClient()) && key.equalsIgnoreCase(other.getKey());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(id)
            .toHashCode();
    }

}
