package com.tsm.sendemail.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.ClientHosts;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile(value = "prod")
@Slf4j
public class InitialLoad implements ApplicationListener<ApplicationReadyEvent> {

	private static final String EMAIL_SERVICE_ENDPOINT = "/api/v1/messages";

	protected static final String COMMA_SEPARATOR = ",";

	@Autowired
	private ClientHostsService clientHostsService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				log.info("Loading allowedOrigins permissions ->");

				Set<ClientHosts> clientsHosts = clientHostsService.findByClientStatus(ClientStatus.ACTIVE);

				if (!clientsHosts.isEmpty()) {
					StringBuffer hosts = new StringBuffer();

					clientsHosts.stream().map(ClientHosts::getHost).forEach(h -> {
						log.info("allowing origin from [{}].", h);
						hosts.append(h);
						hosts.append(COMMA_SEPARATOR);
					});

					String content = hosts.toString();
					content = content.substring(0, content.length() - 1);

					log.info("origins to allowed [{}].", content);
					registry.addMapping(EMAIL_SERVICE_ENDPOINT).allowedOrigins(content);

				} else {
					log.info("None active client host found :( .");
				}

				log.info("Loading allowedOrigins permissions <-");
			}
		};
	}

}
