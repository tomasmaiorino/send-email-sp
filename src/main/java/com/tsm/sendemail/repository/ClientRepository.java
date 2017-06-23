package com.tsm.sendemail.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.sendemail.model.Client;

@Transactional(propagation = Propagation.MANDATORY)
public interface ClientRepository extends Repository<Client, Integer> {

    Client save(Client client);
    
    Optional<Client> findByToken(final String token);

}
