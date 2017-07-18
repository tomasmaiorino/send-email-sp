package com.tsm.sendemail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssertClientRequest {

    @Value(value = "${client.service.key}")
    private String clientServiceKey;

    @Value(value = "${client.service.url}")
    private String clientServiceUrl;

    public boolean isRequestAllowedCheckingRequestUrl(final String requestUrl, final String adminToken) {
        Assert.hasText(requestUrl, "The requestUrl must not be null or empty!");
        Assert.hasText(adminToken, "The adminToken must not be null or empty!");
        log.info("Checking request url for unregistered requests [{}].", requestUrl);
        log.info("Checking adminToken for unregistered requests [{}].", adminToken);

        boolean allowedClient = clientServiceKey.equals(adminToken) && requestUrl.contains(clientServiceUrl);

        log.debug("Allowed request [{}].", allowedClient);

        return allowedClient;
    }

    public boolean isRequestAllowedCheckingAdminToken(final String adminToken) {
        Assert.hasText(adminToken, "The adminToken must not be null or empty!");
        log.info("Checking request token for unregistered requests [{}].", adminToken);

        boolean allowedClient = clientServiceKey.equals(adminToken);

        log.debug("Allowed request [{}].", allowedClient);

        return allowedClient;
    }

}
