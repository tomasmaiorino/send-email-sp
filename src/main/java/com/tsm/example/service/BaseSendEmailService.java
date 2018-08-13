package com.tsm.example.service;

import com.tsm.example.exceptions.MessageException;
import com.tsm.example.model.Message;

public abstract class BaseSendEmailService {

	public abstract Message sendTextEmail(final Message message) throws MessageException;

}
