package com.tsm.sendemail.service;

import static com.tsm.sendemail.util.ErrorCodes.MESSAGE_NOT_FOUND;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.repository.MessageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class MessageService {

    @Autowired
    private MessageRepository repository;

    @Transactional
    public Message save(final Message message) {
        Assert.notNull(message, "The message must not be null.");
        log.info("Saving message [{}] .", message);

        repository.save(message);

        log.info("Saved message [{}].", message);
        return message;
    }

    @Transactional
    public Message update(final Message message) {
        Assert.notNull(message, "The message must not be null.");
        log.info("Updating message [{}] .", message);

        repository.save(message);

        log.info("Updated message [{}].", message);

        return message;
    }

    public Message findByIdAndClient(final Long id, final Client client) {
        Assert.notNull(id, "The id must not be null!");
        Assert.notNull(client, "The client must not be null!");
        log.info("Searching for a message by id [{}] and client [{}].", id, client);

        Message message = repository.findByIdAndClient(id, client)
            .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_NOT_FOUND));

        log.info("Message found [{}].", message);

        return message;
    }
}
