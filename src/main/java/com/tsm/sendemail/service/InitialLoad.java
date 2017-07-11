package com.tsm.sendemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile(value = "prod")
@Slf4j
public class InitialLoad implements ApplicationListener<ApplicationReadyEvent> {

    private static final String EMAIL_SERVICE_ENDPOINT = "/**";

    @Autowired
    private AllowedHostsService allowedHostsService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                log.info("Loading allowedOrigins permissions ->");
//
//                String content = allowedHostsService.loadCrossOriginHosts();
//
//                log.info("origins to allowed [{}].", content);
//
//                registry.addMapping(EMAIL_SERVICE_ENDPOINT).allowedOrigins(content)
//                    .allowedMethods("*")
//                    .allowCredentials(true)
//                    .exposedHeaders("Access-Control-Allow-Origin")
//                    .allowedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "*", "origin", "content-type",
//                        "accept", "x-requested-with");
//
//                log.info("Loading allowedOrigins permissions <-");
//            }
//        };
//    }

}
