package com.tsm.sendemail.controller;

import static com.tsm.sendemail.util.ErrorCodes.REPORT_NOT_SENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.Message.MessageStatus;
import com.tsm.sendemail.parser.ClientParser;
import com.tsm.sendemail.parser.IParser;
import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.service.BaseService;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.service.EmailServiceStatusService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/clients")
@Slf4j
public class ClientsController extends RestBaseController<ClientResource, Client, Integer> {

	@Autowired
	private ClientService service;

	@Autowired
	private ClientParser parser;

	@Autowired
	private EmailServiceStatusService emailServiceStatusService;

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	@ResponseBody
	public ClientResource save(@RequestBody final ClientResource resource, final HttpServletRequest request) {
		return super.save(resource);
	}

	private void assertClientHeader(HttpServletRequest request) {
	}

	@RequestMapping(method = GET, path = "/report")
	@ResponseStatus(OK)
	public void generateReport(HttpServletRequest request) {
		log.debug("Recieved a request to generate the daily report for the admin token.");

		assertClientHeader(request);

		Message message = emailServiceStatusService.checkingDailyEmailsStatus();

		if (message == null || !message.getStatus().equals(MessageStatus.SENT)) {
			throw new BadRequestException(REPORT_NOT_SENT);
		}

		log.debug("Genarate report done.");

	}

	@Override
	public BaseService<Client, Integer> getService() {
		return service;
	}

	@Override
	public IParser<ClientResource, Client> getParser() {
		return parser;
	}
}
