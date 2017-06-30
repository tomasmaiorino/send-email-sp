package com.tsm.sendemail.service;

import static com.tsm.sendemail.util.ErrorCodes.ERROR_SENDING_EMAIL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tsm.sendemail.exceptions.MessageException;
import com.tsm.sendemail.model.Message;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Qualifier("mailgunService")
public class MailgunSendEmailService extends BaseSendEmailService {

    @Getter
    @Setter
    @Value("${sendemail.service.mailgun.serviceEndpoint}")
    private String serviceEndpoint;

    @Getter
    @Setter
    @Value("${sendemail.service.mailgun.mailgunKey}")
    private String mailgunKey;

    @Getter
    @Setter
    @Value("${sendemail.service.mailgun.mailgunDomain}")
    private String mailgunDomain;

    @Override
    public Message sendTextEmail(final Message message) throws MessageException {
        Assert.notNull(message, "The message must not be null!");
        log.debug("sending text email -> [{}]", message);
        try {

            Client client = Client.create();

            WebResource webResource = client.resource(getUrl());

            String input = buildMailgunTextMessage(message);

            log.info("Calling url [{}].", getUrl());
            log.info("Message [{}].", input);

            ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);

            log.info("sendTextEmail response status [{}]", response.getStatus());

            message.setResponseCode(response.getStatus());

            if (response.getStatus() != HttpStatus.CREATED.ordinal()) {
                log.info("Error sending message [{}].", response.getEntity(String.class));
            }

        } catch (Exception e) {
            log.error("Error sending message [{}]", message.getId(), e);
            throw new MessageException(ERROR_SENDING_EMAIL);
        }
        log.debug("sending text email <- [{}]", "message sent");
        return message;
    }

    private String buildMailgunTextMessage(final Message message) {
        MailgunTextMessage mailgunTextMessage = new MailgunTextMessage(message.getSenderEmail(), message.getSubject(),
            message.getClient().getEmailRecipient(), message.getMessage());
        GsonBuilder build = new GsonBuilder();
        return build.create().toJson(mailgunTextMessage);
    }

    private String getUrl() {
        return getServiceEndpoint().replace("#key", getMailgunKey()).replace("#domain_name", getMailgunDomain());
    }

    private class MailgunTextMessage {

        public MailgunTextMessage(final String from, final String subject, final String to, final String text) {
            this.from = from;
            this.subject = subject;
            this.text = text;
            this.to = to;
        }

        @Getter
        private String from;
        @Getter
        private String to;
        @Getter
        private String subject;
        @Getter
        private String text;
    }

}
