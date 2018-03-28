package com.tsm.sendemail.security;

import static com.tsm.sendemail.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tsm.sendemail.exceptions.ForbiddenRequestException;
import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.model.User;
import com.tsm.sendemail.service.ClientService;
import com.tsm.sendemail.service.JwtTokenUtil;
import com.tsm.sendemail.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("#{'${urls.skip.host.validation}'.split(',')}")
	// @Value(value = "${urls.skip.host.validation}")
	private List<String> urlSkipHostValidation;

	@Autowired
	private ClientService clientService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String header = req.getHeader(SecurityConstants.HEADER_STRING);
		String userEmail = null;
		String authToken = null;

		try {
			configureCors(req, res);
		} catch (ForbiddenRequestException e) {
			res.sendError(HttpStatus.SC_FORBIDDEN);
			return;
		}

		if (header != null && header.startsWith(TOKEN_PREFIX)) {
			authToken = header.replace(TOKEN_PREFIX, "");
			try {
				userEmail = jwtTokenUtil.getUserEmailFromToken(authToken);
			} catch (IllegalArgumentException e) {
				log.error("An error occured during getting username from token.", e);
			} catch (ExpiredJwtException e) {
				log.warn("The token is expired and not valid anymore.", e);
			} catch (MalformedJwtException e) {
				log.warn(String.format("The token is malformed [%s].", authToken), e);
			} catch (Exception e) {
				log.warn(String.format("The token is invalid [%s].", authToken), e);
			}
		} else {
			log.warn("Couldn't find bearer string, will ignore the header.");
		}
		if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {

			User user = userService.findByEmail(userEmail);

			if (jwtTokenUtil.validateToken(authToken, user.getEmail())) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						user.getEmail(), null, Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				log.info("Authenticated user [{}] setting security context.", userEmail);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(req, res);
	}

	private void configureCors(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ForbiddenRequestException {

		log.debug("Configuring cors [{}].", request.getRequestURI());

		if (validateHost(request.getRequestURI())) {
			String clientHost = getClientHost(request);
			if (!StringUtils.isBlank(clientHost)) {
				log.debug("Should allow host [{}].", clientHost);
				response.setHeader("Access-Control-Allow-Origin", clientHost);
				response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
				response.setHeader("Access-Control-Expose-Headers", "Location");
			} else {
				log.info("Should not allow host [{}].", clientHost);
				log.debug("None header should be added to the host [{}].", request.getRequestURL().toString());
				throw new ForbiddenRequestException(String.valueOf(HttpStatus.SC_FORBIDDEN));
			}
		} else {
			log.debug("Skiping host verification");
		}

	}

	private boolean validateHost(final String requestUri) {
		urlSkipHostValidation.forEach(u -> log.info("url [{}]", u));
		return urlSkipHostValidation.stream().filter(u -> requestUri.contains(u)).count() == 0l;

	}

	private String getClientHost(final HttpServletRequest req) {
		String host = null;
		String token = "";
		try {
			String request = req.getRequestURI();
			log.debug("parsing request [{}].", request);

			token = request.substring(request.lastIndexOf("/") + 1, request.length());
			log.info("looking for a client with the token [{}].", token);

			Client client = clientService.findByToken(token);
			host = client.getClientHosts().iterator().next().getHost();

		} catch (Exception e) {
			log.info("Client not found [{}].", token);
		}
		return host;
	}
}