package com.tsm.sendemail.controller;

import static com.tsm.sendemail.util.ErrorCodes.INVALID_HOST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.parser.MessageParser;
import com.tsm.sendemail.resources.MessageResource;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.service.MessageService;
import com.tsm.sendemail.service.SendEmailService;

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
    private SendEmailService sendEmailService;

    @Autowired
    private MessageParser parser;

    @RequestMapping(method = POST, path = "/{clientToken}", consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public MessageResource save(@PathVariable String clientToken, @RequestBody final MessageResource resource,
        HttpServletRequest request) {
        log.debug("Recieved a request to create a message [{}] for the client token [{}].", resource, clientToken);

        validate(resource, Default.class);

        Client client = clientService.findByToken(clientToken);

        assertClientHost(client, request);

        Message message = parser.toModel(resource, client);

        message = service.save(message);

        message = sendEmailService.sendTextEmail(message);

        MessageResource result = parser.toResource(message);

        log.debug("returnig resource [{}].", result);

        return result;
    }

    private void assertClientHost(final Client client, HttpServletRequest request) {
        String host = recoverHost(request);
        log.info("checking host request [{}]", host);
        if (client.getClientHosts().stream().filter(h -> h.getHost().equals(host)).count() == 0) {
            throw new BadRequestException(INVALID_HOST);
        }
    }

    private String recoverHost(HttpServletRequest request) {
        log.debug("recovering url [{}] and uri [{}]", request.getRequestURL(), request.getRequestURI());
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.toString().replace(request.getRequestURI(), "");
    }

}
