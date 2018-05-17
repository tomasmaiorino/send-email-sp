package com.tsm.sendemail.controller;

import static com.tsm.sendemail.util.ErrorCodes.INVALID_HOST;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_SEARCH_PARAMS;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.Message;
import com.tsm.sendemail.model.SearchCriteria;
import com.tsm.sendemail.parser.IParser;
import com.tsm.sendemail.parser.MessageParser;
import com.tsm.sendemail.resources.MessageResource;
import com.tsm.sendemail.service.BaseService;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.service.MessageService;
import com.tsm.sendemail.service.SendEmailService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/messages/{clientToken}")
@Slf4j
public class MessagesController extends RestBaseController<MessageResource, Message, Long> {

	@Autowired
	private MessageService service;

	@Autowired
	private ClientService clientService;

	@Autowired
	private SendEmailService sendEmailService;

	@Autowired
	private MessageParser parser;

	private List<String> searchParamsAllowed = Arrays.asList("subject", "status");

	@RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE, headers = {
			"content-type=application/json" })
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

	@RequestMapping(method = GET, produces = JSON_VALUE)
	@ResponseStatus(OK)
	public Set<MessageResource> findAll(@RequestParam(value = "search", required = false) String search) {
		log.debug("Searching for messages [{}].", search);

		List<SearchCriteria> params = new ArrayList<>();

		if (search != null) {
			Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
			Matcher matcher = pattern.matcher(search + ",");
			while (matcher.find()) {
				if (!searchParamsAllowed.contains(matcher.group(1))) {
					throw new BadRequestException(INVALID_SEARCH_PARAMS);
				}
				params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
			}
		}
		List<Message> messages = service.search(params);

		if (!CollectionUtils.isEmpty(messages)) {
			return parser.toResources(new HashSet<>(messages));
		}
		log.info("None messages was found. Returning empty set.");
		return Collections.emptySet();
	}

	private void assertClientHost(final Client client, final HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		log.debug("referer [{}]", referer);
		String parsedHost = parserHost(referer);
		if (client.getClientHosts().stream().filter(h -> parsedHost.equals(parserHost(h.getHost()))).count() == 0) {
			throw new BadRequestException(INVALID_HOST);
		}
	}

	private String parserHost(String host) {
		String parsedHost = "";

		if (!host.contains("http")) {
			host = "http://" + host;
		}
		try {
			URL url = new URL(host);
			parsedHost = url.getHost();
			log.debug("Parsed host [{}]. ", parsedHost);
		} catch (Exception e) {
			log.error("Error trying to parse host [{}].", host, e);
		}
		return parsedHost;
	}

	@Override
	public BaseService<Message, Long> getService() {
		return service;
	}

	@Override
	public IParser<MessageResource, Message> getParser() {
		return parser;
	}

}
