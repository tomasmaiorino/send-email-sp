package com.tsm.sendemail.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.ClientHosts;

@Transactional(propagation = Propagation.MANDATORY)
public interface ClientHostsRepository extends Repository<ClientHosts, Integer> {

    @Query("SELECT c FROM ClientHosts c WHERE c.client.status = ?1")
    Set<ClientHosts> findByClientStatus(final ClientStatus status);

}
