package com.tsm.sendemail.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;

@Transactional(propagation = Propagation.MANDATORY)
public interface MessageRepository extends CrudRepository<Message, Long>,
        IBaseRepository<Message, Long>, IBaseSearchRepositoryCustom<Message> {

    Optional<Message> findByIdAndClient(final Long id, final Client client);

    Set<Message> findByClientAndCreatedBetween(final Client client, final LocalDateTime initialDate,
                                               final LocalDateTime finalDate);
}
