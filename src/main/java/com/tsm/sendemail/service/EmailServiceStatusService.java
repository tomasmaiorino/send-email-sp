package com.tsm.sendemail.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

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

	@Autowired
	private MessageService messageService;

	@Autowired
	private SendEmailService sendEmailService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private BuildStatusEmailContentService buildStatusEmailContentService;

	private LocalDateTime initialDate;

	private LocalDateTime finalDate;

	private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

	@Scheduled(cron = "0 0 0 * * *")
	// @Scheduled(initialDelay = 1000, fixedRate = 20000)
	public void checkingDailyEmailsStatus() {
		log.info("checkingDailyEmailsStatus ->");

		initialDate = LocalDateTime.now().minusHours(24);
		finalDate = LocalDateTime.now();

		Set<Client> activeClients = clientService.findByStatus(ClientStatus.ACTIVE);

		log.info("Active clients found [{}].", activeClients.size());

		String messageContent = "";

		if (!activeClients.isEmpty()) {

			messageContent = buildStatusEmailContentService.buildMessage(initialDate, finalDate, activeClients);

			log.debug("Message content [{}].", messageContent);

			sendEmail(messageContent, initialDate, finalDate);
		}

		log.info("checkingDailyEmailsStatus <-");
	}

	private void sendEmail(final String messageContent, final LocalDateTime initialDate,
			final LocalDateTime finalDate) {
		Message message = createMessage(messageContent, initialDate, finalDate);
		try {
			sendEmailService.sendTextEmail(message);
		} catch (Exception e) {
			log.error("Error sending status email.", e);
		}

	}

	private Message createMessage(final String messageContent, final LocalDateTime initialDate,
			final LocalDateTime finalDate) {

		Message message = new Message();
		Client client = clientService.findByIsAdmin(true).iterator().next();
		message.setClient(client);
		message.setMessage(messageContent.toString());
		message.setSenderEmail(client.getEmailRecipient());
		message.setSenderName(client.getName());
		message.setSubject(String.format("Email Status: %s", finalDate.format(formatter)));
		message.setStatus(MessageStatus.CREATED);

		return messageService.save(message);
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}

}
