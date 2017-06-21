package com.tsm.sendemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional
    public Client save(final Client client) {
        Assert.notNull(client, "The client must not be null.");
        log.info("Saving client [{}] .", client);

        repository.save(client);

        log.info("Saved client [{}].", client);
        return client;
    }
}
