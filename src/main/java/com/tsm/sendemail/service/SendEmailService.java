package com.tsm.sendemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tsm.sendemail.exceptions.MessageException;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;

import lombok.Getter;
import lombok.Setter;

@Service
public class SendEmailService {

	@Autowired
	@Getter
	@Setter
	private MessageService messageService;

	@Autowired
	@Getter
	@Setter
	@Qualifier("mailgunService")
	private BaseSendEmailService sendEmailService;

	public Message sendTextEmail(Message message) {
		Assert.notNull(message, "The message must not be null!");
		try {
			message = sendEmailService.sendTextEmail(message);
			if (message.getResponseCode() != HttpStatus.OK.value()) {
				message.setStatus(MessageStatus.NOT_SENT);
			} else {
				message.setStatus(MessageStatus.SENT);
			}
		} catch (MessageException e) {
			message.setStatus(MessageStatus.ERROR);
			throw e;
		} finally {
			messageService.update(message, message);
		}
		return message;
	}

}
