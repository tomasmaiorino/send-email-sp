package com.tsm.sendemail.service;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.repository.ClientRepository;
import com.tsm.sendemail.repository.IBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Set;

import static com.tsm.sendemail.util.ErrorCodes.CLIENT_NOT_FOUND;
import static com.tsm.sendemail.util.ErrorCodes.DUPLICATED_TOKEN;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class ClientService extends BaseService<Client, Integer>{

    @Autowired
    private ClientRepository repository;

    @Override
    protected String getClassName() {
        return Client.class.getSimpleName();
    }

    @Override
    public IBaseRepository<Client, Integer> getRepository() {
        return repository;
    }

    protected void saveValidation(final Client model) {
        repository.findByToken(model.getToken()).ifPresent(c -> {
            throw new BadRequestException(DUPLICATED_TOKEN);
        });

    }

    public Client findByToken(final String token) {
        Assert.hasText(token, "The token must not be null or empty.");
        log.info("Finding client by token [{}] .", token);

        Client client = repository.findByToken(token)
            .orElseThrow(() -> new ResourceNotFoundException(CLIENT_NOT_FOUND));

        log.info("Client found [{}].", client);

        return client;
    }

    public Set<Client> findByStatus(final ClientStatus status) {
        Assert.notNull(status, "The status must not be null or empty.");
        log.info("Finding client by status [{}] .", status);

        Set<Client> clients = repository.findByStatus(status);

        log.info("Clients found [{}].", clients.size());

        return clients;
    }

    public Set<Client> findByIsAdmin(final Boolean admin) {
        Assert.notNull(admin, "The admin must not be null or empty.");
        log.info("Finding client by admin [{}] .", admin);

        Set<Client> clients = repository.findByIsAdmin(admin);

        log.info("Clients found [{}].", clients.size());

        return clients;
    }
}
