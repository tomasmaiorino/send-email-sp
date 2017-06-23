package com.tsm.sendemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
}
