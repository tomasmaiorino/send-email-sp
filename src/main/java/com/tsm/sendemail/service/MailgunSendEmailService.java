package com.tsm.sendemail.service;

import static com.tsm.sendemail.util.ErrorCodes.ERROR_SENDING_EMAIL;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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

			log.debug("Endpoint [{}].", getUrl());

			HttpResponse response = doesPostRequest(message);

			log.info("sendTextEmail respons status [{}]", response.getStatusLine().getStatusCode());

			message.setResponseCode(response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
				log.info("Response message [{}]", getMessageResponse(response.getEntity().getContent()));
			}

		} catch (Exception e) {
			log.error("Error sending message [{}]", message.getId(), e);
			throw new MessageException(ERROR_SENDING_EMAIL);
		}

		log.debug("sending text email <- [{}]", "message sent");

		return message;
	}

	@SuppressWarnings("deprecation")
	private HttpResponse doesPostRequest(final Message message)
			throws AuthenticationException, UnsupportedEncodingException, IOException, ClientProtocolException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("api", getMailgunKey());
		HttpPost httpPost = new HttpPost(getUrl());

		httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost));
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Map<String, String> params = buildMailgunTextMessage(message);

		params.forEach((k, v) -> {
			nvps.add(new BasicNameValuePair(k, v));
		});

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));

		return httpclient.execute(httpPost);
	}

	@SuppressWarnings("resource")
	private String getMessageResponse(InputStream inputStream) {
		Scanner s = new Scanner(inputStream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private Map<String, String> buildMailgunTextMessage(final Message message) {
		MailgunTextMessage mailgunTextMessage = new MailgunTextMessage(message.getSenderEmail(), message.getSubject(),
				message.getClient().getEmailRecipient(), message.getMessage());
		return mailgunTextMessage.getParams();
	}

	private String getUrl() {
		return getServiceEndpoint().replaceAll("#domain_name", getMailgunDomain());
	}

	private class MailgunTextMessage {

		public MailgunTextMessage(final String from, final String subject, final String to, final String text) {
			this.from = from;
			this.subject = subject;
			this.text = text;
			this.to = to;
		}

		private Map<String, String> params = new HashMap<>();

		public Map<String, String> getParams() {
			params.put("from", from);
			params.put("subject", subject);
			params.put("text", text);
			params.put("to", to);
			return params;
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
