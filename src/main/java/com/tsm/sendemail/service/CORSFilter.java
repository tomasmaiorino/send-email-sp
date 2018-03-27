package com.tsm.sendemail.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tsm.sendemail.model.Client;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CORSFilter implements Filter {

	@Autowired
	private ClientService clientService;

	@Value(value = "${client.service.url}")
	private String clientServiceUrl;

	@Value(value = "${urls.skip.host.validation}")
	private List<String> urlSkipHostValidation;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		if (!skipUrlValidation(request.getRequestURI())) {
			String clientHost = getClientHost(request);
			if (!StringUtils.isBlank(clientHost)) {
				response.setHeader("Access-Control-Allow-Origin", clientHost);
				response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
				response.setHeader("Access-Control-Expose-Headers", "Location");
			} else {
				log.debug("None header should be added to the host [{}].", request.getRequestURL().toString());
				response.sendError(HttpStatus.SC_FORBIDDEN);
				return;
			}
		} else {
			log.debug("Skiping host verification");
		}

		chain.doFilter(req, res);
	}

	private boolean skipUrlValidation(final String requestUri) {
		return urlSkipHostValidation.stream().filter(u -> requestUri.contains(u)).count() == 0l;
	}

	private String getClientHost(final HttpServletRequest req) {
		String host = null;
		String token = "";
		try {
			String request = req.getRequestURI();
			log.debug("parsing request [{}].", request);

			token = request.substring(request.lastIndexOf("/") + 1, request.length());
			log.info("locking for a client with the token [{}].", token);

			Client client = clientService.findByToken(token);
			host = client.getClientHosts().iterator().next().getHost();

		} catch (Exception e) {
			log.info("Client not found [{}].", token);
		}
		return host;
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
