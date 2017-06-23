package com.tsm.sendemail.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.sendemail.model.Message;

@Transactional(propagation = Propagation.MANDATORY)
public interface MessageRepository extends Repository<Message, Long> {

    Message save(final Message message);

}
