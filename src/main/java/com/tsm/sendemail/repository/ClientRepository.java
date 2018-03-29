package com.tsm.sendemail.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;

@Transactional(propagation = Propagation.MANDATORY)
public interface ClientRepository extends Repository<Client, Integer>, IBaseRepository<Client, Integer> {

    Optional<Client> findByToken(final String token);

    Set<Client> findByStatus(final ClientStatus status);

    Set<Client> findByIsAdmin(final Boolean admin);

}
