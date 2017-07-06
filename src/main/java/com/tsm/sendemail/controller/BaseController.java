package com.tsm.sendemail.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.ClientHosts;
import com.tsm.sendemail.service.ClientHostsService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings(value = { "rawtypes" })
@Slf4j
public class BaseController {

	public static final String JSON_VALUE = "application/json";

	protected static final String COMMA_SEPARATOR = ",";

	@Autowired
	private Validator validator;

	@Autowired
	private ClientHostsService clientHostsService;

	private String allowedOrigins;

	protected <T> void validate(final T object, Class clazz) {
		Set<ConstraintViolation<T>> violations = validator.validate(object, clazz);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@RequestMapping(value = "/api/**", method = RequestMethod.OPTIONS)
	public void corsHeaders(HttpServletResponse response) {
		log.info("checking options call.");
		if (allowedOrigins == null) {
			allowedOrigins = loadlAllowedOrigins();
		}

		if (StringUtils.isNoneBlank(allowedOrigins)) {
			response.addHeader("Access-Control-Allow-Origin", allowedOrigins);
		}

		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
		response.addHeader("Access-Control-Max-Age", "3600");
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

			String content = hosts.toString();
			content = content.substring(0, content.length() - 1);

			log.info("controller origins to allowed [{}].", content);
			
			return content;

		} else {
			log.info("None active client host found :( .");
		}
		return null;
	}
}
