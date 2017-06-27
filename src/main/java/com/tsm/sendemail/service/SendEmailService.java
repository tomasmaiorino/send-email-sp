package com.tsm.sendemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tsm.sendemail.exceptions.MessageException;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;
import org.springframework.util.Assert;
import static com.tsm.sendemail.util.ErrorCodes.ERROR_SENDING_EMAIL;

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
			message.setStatus(MessageStatus.SENT);
		} catch (Exception e) {
			message.setStatus(MessageStatus.ERROR);
			throw new MessageException(ERROR_SENDING_EMAIL);
		} finally {
			messageService.update(message);
		}
		return message;
	}

}
