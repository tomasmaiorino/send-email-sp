package com.tsm.sendemail.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import lombok.Getter;

@Entity
@Table(name = "message")
public class Message extends BaseModel {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    public enum MessageStatus {
        CREATED, SENT, NOT_SENT, ERROR;
    }

    @Getter
    @Column(nullable = false, length = 600)
    private String message;

    @Getter
    @Column(nullable = false, length = 30)
    private String subject;

    @Getter
    @Column(nullable = false, length = 30)
    private String senderName;

    @Getter
    @Column(nullable = false, length = 30)
    private String senderEmail;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Getter
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.CREATED;

    @Transient
    private Integer responseCode;

    public void setMessage(final String message) {
        Assert.hasText(message, "The message must not be null or empty!");
        this.message = message;
    }

    public void setSubject(final String subject) {
        Assert.hasText(subject, "The subject must not be null or empty!");
        this.subject = subject;
    }

    public void setSenderName(final String senderName) {
        Assert.hasText(senderName, "The senderName must not be null or empty!");
        this.senderName = senderName;
    }

    public void setSenderEmail(final String senderEmail) {
        Assert.hasText(senderEmail, "The senderEmail must not be null or empty!");
        this.senderEmail = senderEmail;
    }

    public void setClient(Client client) {
        Assert.notNull(client, "The client must not be null!");
        this.client = client;
    }

    public void setStatus(final MessageStatus status) {
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
        Message other = (Message) obj;
        if (getId() == null || other.getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

}
