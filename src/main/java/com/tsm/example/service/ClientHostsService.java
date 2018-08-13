package com.tsm.example.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.example.model.ClientHosts;
import com.tsm.example.model.Client.ClientStatus;
import com.tsm.example.repository.ClientHostsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ClientHostsService {

    @Autowired
    private ClientHostsRepository repository;

    public Set<ClientHosts> findByClientStatus(final ClientStatus status) {
        Assert.notNull(status, "The status must not be null!");
        log.info("Finding client hosts by status [{}] .", status);

        Set<ClientHosts> clientsHosts = repository.findByClientStatus(status);

        log.info("ClientsHosts found [{}].", clientsHosts.size());

        return clientsHosts;
    }
}
