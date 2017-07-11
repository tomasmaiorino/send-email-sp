package com.tsm.sendemail.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsm.sendemail.model.Client.ClientStatus;
import com.tsm.sendemail.model.ClientHosts;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AllowedHostsService {

    private static final String COMMA_SEPARATOR = ",";

    @Autowired
    private ClientHostsService clientHostsService;

    public String loadCrossOriginHosts() {
        log.info("Loading actives hosts ->");
        String content = null;
        Set<ClientHosts> clientsHosts = clientHostsService.findByClientStatus(ClientStatus.ACTIVE);

        if (!clientsHosts.isEmpty()) {
            StringBuffer hosts = new StringBuffer();

            clientsHosts.stream().map(ClientHosts::getHost).forEach(h -> {
                log.info("host found [{}].", h);
                hosts.append(h);
                hosts.append(COMMA_SEPARATOR);
            });

            content = hosts.toString();
            log.info("hosts found [{}].", content);
        } else {
            log.info("None active client host found :( .");
        }
        log.info("Loading actives hosts <-");

        return content;
    }
}
