package com.tsm.sendemail.controller;

import static com.tsm.sendemail.util.ErrorCodes.ACCESS_NOT_ALLOWED;
import static com.tsm.sendemail.util.ErrorCodes.MISSING_HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.exceptions.ForbiddenRequestException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.parser.ClientParser;
import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.service.AssertClientRequest;
import com.tsm.sendemail.service.ClientService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/clients")
@Slf4j
public class ClientsController extends BaseController {

	public static final String ADMIN_TOKEN_HEADER = "AT";

	@Autowired
	private ClientService service;

	@Autowired
	private ClientParser parser;

	@Autowired
	private AssertClientRequest assertClientRequest;

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
	@ResponseStatus(CREATED)
	public ClientResource save(@RequestBody final ClientResource resource, HttpServletRequest request) {
		log.debug("Recieved a request to create a client [{}].", resource);

		assertClientHeader(request);

		validate(resource, Default.class);

		Client client = parser.toModel(resource);

		client = service.save(client);

		ClientResource result = parser.toResource(client);

		log.debug("returnig resource [{}].", result);

		return result;
	}

	private void assertClientHeader(HttpServletRequest request) {

		if (StringUtils.isBlank(request.getHeader(ADMIN_TOKEN_HEADER))) {
			throw new BadRequestException(MISSING_HEADER);
		}
		
		if (!assertClientRequest.isRequestAllowedCheckingAdminToken(request.getHeader(ADMIN_TOKEN_HEADER))) {
			throw new ForbiddenRequestException(ACCESS_NOT_ALLOWED);
		}

	}

}
