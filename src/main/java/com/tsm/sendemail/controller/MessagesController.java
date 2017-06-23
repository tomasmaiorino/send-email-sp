package com.tsm.sendemail.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.parser.MessageParser;
import com.tsm.sendemail.resources.MessageResource;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/messages")
@Slf4j
public class MessagesController extends BaseController {

	@Autowired
	private MessageService service;

	@Autowired
	private ClientService clientService;

	@Autowired
	private MessageParser parser;

	@RequestMapping(method = POST)
	@ResponseStatus(CREATED)
	public MessageResource save(@PathVariable String clientToken, @RequestBody final MessageResource resource) {
		log.debug("Recieved a request to create a message [{}] for the client token [{}].", resource, clientToken);

		validate(resource, Default.class);

		Client client = clientService.findByToken(clientToken);

		Message message = parser.toModel(resource, client);

		message = service.save(message);

		MessageResource result = parser.toResource(message);

		log.debug("returnig resource [{}].", result);

		return result;
	}

}
