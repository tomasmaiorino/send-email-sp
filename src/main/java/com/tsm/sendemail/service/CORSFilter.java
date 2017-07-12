package com.tsm.sendemail.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsm.sendemail.model.Client;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CORSFilter implements Filter {

    @Autowired
    private ClientService clientService;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        String clientToken = getClientToken(req);
        response.setHeader("Access-Control-Allow-Origin", clientToken);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Expose-Headers", "Location");
        chain.doFilter(req, res);
    }

    private String getClientToken(final ServletRequest req) {
        String host = null;
        String token = "";
        try {
            String request = ((HttpServletRequest) req).getRequestURI();
            log.info("parsing request [{}].", request);

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
