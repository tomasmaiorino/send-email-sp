package com.tsm.sendemail.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import lombok.Getter;

@Entity
@Table(name = "client_hosts")
public class ClientHosts extends BaseModel {

    @Getter
    private String host;

    public void setHost(final String host) {
        Assert.hasText(host, "The host must not be null!");
        this.host = host;
    }

}
