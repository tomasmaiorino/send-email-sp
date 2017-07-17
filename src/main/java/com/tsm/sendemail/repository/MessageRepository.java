package com.tsm.sendemail.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.Message;

@Transactional(propagation = Propagation.MANDATORY)
public interface MessageRepository extends Repository<Message, Long> {

	Message save(final Message message);

	Optional<Message> findByIdAndClient(final Long id, final Client client);

	Set<Message> findByClientStatusAndCreatedBetween(final ClientStatus status, final LocalDateTime initialDate,
			final LocalDateTime finalDate);

}
