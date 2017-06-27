package com.tsm.sendemail.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsm.sendemail.model.Client;
import com.tsm.sendemail.parser.ClientParser;
import com.tsm.sendemail.resources.ClientResource;
import com.tsm.sendemail.service.ClientService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/clients")
@Slf4j
public class ClientsController extends BaseController {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientParser parser;

    @RequestMapping(method = POST,
        consumes = JSON_VALUE,
        produces = JSON_VALUE)
    @ResponseStatus(CREATED)
    public ClientResource save(@RequestBody final ClientResource resource) {
        log.debug("Recieved a request to create a client [{}].", resource);

        validate(resource, Default.class);

        Client client = parser.toModel(resource);

        client = service.save(client);

        ClientResource result = parser.toResource(client);

        log.debug("returnig resource [{}].", result);

        return result;
    }

}
