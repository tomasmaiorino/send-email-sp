package com.tsm.sendemail.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.gson.GsonBuilder;
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

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(getUrl());
			StringEntity entity = new StringEntity(buildMailgunTextMessage(message), ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);

			CloseableHttpResponse response = httpclient.execute(httpPost);
			log.info("sendTextEmail responsa status [{}]", response.getStatusLine().getStatusCode());

		} catch (Exception e) {
			log.error("Error sending message [{}]", message.getId(), e);
			throw new MessageException("");
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
