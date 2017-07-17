package com.tsm.sendemail.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.util.ClientTestBuilder;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile(value = "prod")
@Slf4j
public class EmailServiceStatusService {

	private static final Object CLIENT_LABEL = null;
	private static final Object COLON_SEPARATOR = null;
	private static final Object ERROR = null;
	private static final Object NOT_SENT_LABEL = null;
	private static final Object SENT_LABEL = null;
	private static final Object CREATED = null;

	@Autowired
	private MessageService messageService;

	@Scheduled(fixedRate = 5000)
	public void checkingDailyEmailsStatus() {
		log.info("checkingDailyEmailsStatus ->");

		Set<Message> messages = messageService.findByClientStatusAndCreatedBetween(ClientStatus.ACTIVE,
				LocalDateTime.now(), LocalDateTime.now());
		log.info("Messages found [{}].", messages.size());

		if (!messages.isEmpty()) {

		}

		log.info("checkingDailyEmailsStatus <-");

	}

	private StringBuffer buildClientSection(final Client client) {
		StringBuffer sb = new StringBuffer();
		sb.append(CLIENT_LABEL);
		sb.append(COLON_SEPARATOR);
		sb.append(" " + client.getName());
		sb.append(System.lineSeparator());
		return sb;
	}

	// private String formaMessage(final Set<Message> messages) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(CLIENT_LABEL);
	// sb.append(COLON_SEPARATOR);
	// sb.append(" " + message.getClient().getName());
	// sb.append(System.lineSeparator());
	// sb.append(CREATED);
	// sb.append(COLON_SEPARATOR);
	// sb.append(" " + message.getClient().getName());
	// sb.append(System.lineSeparator());
	// sb.append(SENT_LABEL);
	// sb.append(COLON_SEPARATOR);
	// sb.append(" " + message.getClient().getName());
	// sb.append(System.lineSeparator());
	// sb.append(NOT_SENT_LABEL);
	// sb.append(COLON_SEPARATOR);
	// sb.append(" " + message.getClient().getName());
	// sb.append(System.lineSeparator());
	// sb.append(ERROR);
	// sb.append(COLON_SEPARATOR);
	// sb.append(" " + message.getClient().getName());
	// sb.append(System.lineSeparator());
	// }
}
