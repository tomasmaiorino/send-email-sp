package com.tsm.sendemail.service;

import com.tsm.sendemail.exceptions.ResourceNotFoundException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.SearchCriteria;
import com.tsm.sendemail.repository.IBaseRepository;
import com.tsm.sendemail.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.tsm.sendemail.util.ErrorCodes.MESSAGE_NOT_FOUND;

@Service
@Slf4j
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class MessageService extends BaseService<Message, Long> {

	@Autowired
	private MessageRepository repository;

	@Override
	protected void merge(final Message origin, final Message model) {
		origin.setClient(model.getClient());
		origin.setMessage(model.getMessage());
		origin.setSenderEmail(model.getSenderEmail());
		origin.setSenderName(model.getSenderName());
		origin.setSubject(model.getSubject());
		origin.setStatus(model.getStatus());
	}

	public List<Message> search(final List<SearchCriteria> criterias) {
		Assert.notEmpty(criterias, "The criterias must not be null nor empty!");
		log.debug("Searching for message using the follwing criterias: [{}]", criterias);
		return repository.search(criterias, Message.class);
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

	public Set<Message> findByClientAndCreatedBetween(final Client client, final LocalDateTime initialDate,
			final LocalDateTime finalDate) {
		Assert.notNull(client, "The client must not be null.");
		Assert.notNull(initialDate, "The initialDate must not be null.");
		Assert.notNull(finalDate, "The finalDate must not be null.");

		Set<Message> messages = repository.findByClientAndCreatedBetween(client, initialDate, finalDate);

		log.info("Messages found [{}].", messages.size());

		return messages;
	}

	@Override
	public IBaseRepository<Message, Long> getRepository() {
		return repository;
	}

	@Override
	protected String getClassName() {
		return Message.class.getSimpleName();
	}
}
