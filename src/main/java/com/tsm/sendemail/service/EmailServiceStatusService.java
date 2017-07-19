package com.tsm.sendemail.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile(value = "prod")
@Slf4j
public class EmailServiceStatusService {

	private static final String CLIENT_LABEL = "Client";
	private static final String COLON_SEPARATOR = ":";
	private static final String ERROR = "Error";
	private static final String NOT_SENT_LABEL = "Not Sent";
	private static final String SENT_LABEL = "Sent";
	private static final String CREATED = "Created";
	private static final String CLIENT_SEPARATOR = "-----";
	private static final String CLIENT_MESSAGE_SEPARATOR = "--";

	@Autowired
	private MessageService messageService;

	@Autowired
	private SendEmailService sendEmailService;

	@Autowired
	private ClientService clientService;

	private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

	@Scheduled(fixedRate = 5000)
	public void checkingDailyEmailsStatus() {
		log.info("checkingDailyEmailsStatus ->");
		LocalDateTime initialDate = LocalDateTime.now().minusHours(24);
		LocalDateTime finalDate = LocalDateTime.now();

		Set<Client> activeClients = clientService.findByStatus(ClientStatus.ACTIVE);

		log.info("Active clients found [{}].", activeClients.size());

		StringBuilder messageContent = new StringBuilder();
		messageContent.append(CLIENT_SEPARATOR);

		if (!activeClients.isEmpty()) {

			buildingMessageContent(initialDate, finalDate, activeClients, messageContent);

			if (messageContent.length() == 0) {
				messageContent.append("None messages has been sent.");
			}

			sendEmail(messageContent, initialDate, finalDate);
		}
		log.info("checkingDailyEmailsStatus <-");
	}

	private void buildingMessageContent(LocalDateTime initialDate, LocalDateTime finalDate, Set<Client> activeClients,
			StringBuilder messageContent) {
		log.info("Building email content ->");

		activeClients.forEach(c -> {
			StringBuilder clientHeader = buildClientSection(c);
			StringBuilder messagesContent = new StringBuilder();

			Set<Message> clientMessages = messageService.findByClientAndCreatedBetween(c,
					LocalDateTime.now().minusHours(24), LocalDateTime.now());

			log.info("Messages found [{}] for the client [{}] between [{}] and [{}].", clientMessages.size(),
					c.getName(), initialDate.format(formatter), finalDate.format(formatter));

			if (!clientMessages.isEmpty()) {
				messagesContent = formaMessage(clientMessages);
			}

			messageContent.append(clientHeader);
			messageContent.append(CLIENT_MESSAGE_SEPARATOR);
			messageContent.append(messagesContent);

		});

		log.info("Building email content <-");
	}

	private void sendEmail(final StringBuilder messageContent, final LocalDateTime initialDate,
			final LocalDateTime finalDate) {
		Message message = createMessage(messageContent, initialDate, finalDate);
		try {
			sendEmailService.sendTextEmail(message);
		} catch (Exception e) {
			log.error("Error sending status email.", e);
		}

	}

	private Message createMessage(final StringBuilder messageContent, final LocalDateTime initialDate,
			final LocalDateTime finalDate) {
		Message message = new Message();
		Client client = clientService.findByToken("");
		message.setClient(client);
		message.setMessage(messageContent.toString());
		message.setSenderEmail(client.getEmailRecipient());
		message.setSenderName(client.getName());
		message.setSubject(
				String.format("Send Email Status %s - %s", initialDate.format(formatter), finalDate.format(formatter)));
		message.setStatus(MessageStatus.CREATED);

		return messageService.save(message);
	}

	private StringBuilder buildClientSection(final Client client) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLIENT_LABEL);
		sb.append(COLON_SEPARATOR);
		sb.append(" " + client.getName());
		sb.append(System.lineSeparator());
		return sb;
	}

	private StringBuilder formaMessage(final Set<Message> messages) {
		StringBuilder sb = new StringBuilder();
		sb.append(System.lineSeparator());
		sb.append(CREATED);
		sb.append(COLON_SEPARATOR);
		sb.append(" " + messages.stream().filter(m -> m.getStatus().equals(MessageStatus.CREATED)).count());
		sb.append(System.lineSeparator());
		sb.append(SENT_LABEL);
		sb.append(COLON_SEPARATOR);
		sb.append(" " + messages.stream().filter(m -> m.getStatus().equals(MessageStatus.SENT)).count());
		sb.append(System.lineSeparator());
		sb.append(NOT_SENT_LABEL);
		sb.append(COLON_SEPARATOR);
		sb.append(" " + messages.stream().filter(m -> m.getStatus().equals(MessageStatus.NOT_SENT)).count());
		sb.append(System.lineSeparator());
		sb.append(ERROR);
		sb.append(COLON_SEPARATOR);
		sb.append(" " + messages.stream().filter(m -> m.getStatus().equals(MessageStatus.ERROR)).count());
		sb.append(System.lineSeparator());
		return sb;
	}
}
