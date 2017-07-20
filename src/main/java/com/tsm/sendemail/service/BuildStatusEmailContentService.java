package com.tsm.sendemail.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildStatusEmailContentService {

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

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public String buildMessage(final LocalDateTime initialDate, final LocalDateTime finalDate, final Set<Client> activeClients) {
        Assert.notNull(initialDate, "The initialDate must not be null!");
        Assert.notNull(finalDate, "The finalDate must not be null!");
        Assert.notEmpty(activeClients, "The activeClients must not be empty!");

        StringBuilder messageContent = new StringBuilder();
        messageContent.append(CLIENT_SEPARATOR);
        messageContent.append(System.lineSeparator());

        buildingMessageContent(initialDate, finalDate, activeClients, messageContent);

        if (messageContent.length() == 0) {
            messageContent.append("None messages has been sent.");
        }

        return messageContent.toString();
    }

    private void buildingMessageContent(LocalDateTime initialDate, LocalDateTime finalDate, Set<Client> activeClients,
        StringBuilder messageContent) {
        log.info("Building email content ->");

        activeClients.forEach(c -> {
            StringBuilder clientHeader = buildClientSection(c);
            StringBuilder messagesContent = new StringBuilder();

            Set<Message> clientMessages = messageService.findByClientAndCreatedBetween(c,
                initialDate, finalDate);

            log.info("Messages found [{}] for the client [{}] between [{}] and [{}].", clientMessages.size(),
                c.getName(), initialDate.format(formatter), finalDate.format(formatter));

            if (!clientMessages.isEmpty()) {
                messagesContent = formaMessage(clientMessages);
            } else {
                clientHeader.append(String.format("None messages has been sent to the client [%s].", c.getName()));
                clientHeader.append(System.lineSeparator());
            }

            messageContent.append(clientHeader);
            messageContent.append(CLIENT_MESSAGE_SEPARATOR);
            messageContent.append(messagesContent);
            messageContent.append(CLIENT_SEPARATOR);
            messageContent.append(System.lineSeparator());
        });

        log.info("Building email content <-");
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
