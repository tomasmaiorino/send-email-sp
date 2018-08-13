package com.tsm.example.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.example.model.ClientHosts;
import com.tsm.example.model.Client.ClientStatus;

@Transactional(propagation = Propagation.MANDATORY)
public interface ClientHostsRepository extends Repository<ClientHosts, Integer> {

    @Query("SELECT c FROM ClientHosts c WHERE c.client.status = ?1")
    Set<ClientHosts> findByClientStatus(final ClientStatus status);

}
