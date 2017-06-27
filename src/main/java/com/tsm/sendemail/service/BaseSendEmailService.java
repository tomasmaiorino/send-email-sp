package com.tsm.sendemail.service;

import com.tsm.sendemail.exceptions.MessageException;
import com.tsm.sendemail.model.Message;

public abstract class BaseSendEmailService {

	public abstract Message sendTextEmail(final Message message) throws MessageException;

}
