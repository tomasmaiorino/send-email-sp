package com.tsm.sendemail.controller;

import static com.tsm.sendemail.util.ErrorCodes.INVALID_HOST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.ClientHosts;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.parser.MessageParser;
import com.tsm.sendemail.resources.MessageResource;
import com.tsm.sendemail.service.ClientHostsService;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.service.MessageService;
import com.tsm.sendemail.service.SendEmailService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/messages/{clientToken}")
@Slf4j
public class MessagesController extends BaseController {

    @Autowired
    private MessageService service;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SendEmailService sendEmailService;
    
	@Autowired
	private ClientHostsService clientHostsService;

	private String allowedOrigins;

    @Autowired
    private MessageParser parser;

    @RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
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

    @RequestMapping(method = GET, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public MessageResource findById(@PathVariable String clientToken, @PathVariable Long id,
        HttpServletRequest request) {
        log.debug("Recieved a request to search for a message by id [{}] and client token [{}].", id, clientToken);

        Client client = clientService.findByToken(clientToken);

        assertClientHost(client, request);

        Message message = service.findByIdAndClient(id, client);

        MessageResource result = parser.toResource(message);

        log.debug("returnig resource [{}].", result);

        return result;
    }

	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity corsHeaders(HttpServletResponse response) {
		log.info("checking options call.");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private String loadlAllowedOrigins() {

		Set<ClientHosts> clientsHosts = clientHostsService.findByClientStatus(ClientStatus.ACTIVE);

		if (!clientsHosts.isEmpty()) {
			StringBuffer hosts = new StringBuffer();

			clientsHosts.stream().map(ClientHosts::getHost).forEach(h -> {
				log.info("controller allowing origin from [{}].", h);
				hosts.append(h);
				hosts.append(COMMA_SEPARATOR);
			});

			String content = hosts.toString() + "https://mighty-woodland-49949.herokuapp.com";
			//content = content.substring(0, content.length() - 1);

			log.info("controller origins to allowed [{}].", content);
			
			return content;

		} else {
			log.info("None active client host found :( .");
		}
		return null;
	}
    
    private void assertClientHost(final Client client, final HttpServletRequest request) {
        String host = recoverHost(request);
        log.info("checking host request [{}].", host);
        if (client.getClientHosts().stream().filter(h -> h.getHost().equals(host)).count() == 0) {
            throw new BadRequestException(INVALID_HOST);
        }
    }

    private String recoverHost(final HttpServletRequest request) {
        log.debug("checking request received: url [{}] and uri [{}].", request.getRequestURL(), request.getRequestURI());
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.toString().replace(request.getRequestURI(), "");
    }

}
