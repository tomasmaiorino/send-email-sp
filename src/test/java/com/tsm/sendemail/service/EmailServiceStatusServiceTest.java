package com.tsm.sendemail.service;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@FixMethodOrder(MethodSorters.JVM)
public class EmailServiceStatusServiceTest {

    @Mock
    private MessageService messageService;

    @Mock
    private SendEmailService sendEmailService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private EmailServiceStatusService service;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}
