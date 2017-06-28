package com.tsm.sendemail.resources;

import static com.tsm.sendemail.util.ErrorCodes.FIELD_REQUIRED;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_MESSAGE_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_SENDER_NAME_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_SUBJECT_SIZE;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import lombok.Getter;
import lombok.Setter;

public class MessageResource extends BaseResource {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotNull(message = FIELD_REQUIRED)
    @Size(min = 2, max = 300, message = INVALID_MESSAGE_SIZE)
    private String message;

    @Getter
    @Setter
    @NotNull(message = FIELD_REQUIRED)
    @Size(min = 2, max = 30, message = INVALID_SUBJECT_SIZE)
    private String subject;

    @Getter
    @Setter
    @NotNull(message = FIELD_REQUIRED)
    @Size(min = 2, max = 20, message = INVALID_SENDER_NAME_SIZE)
    private String senderName;

    @Getter
    @Setter
    @NotNull(message = FIELD_REQUIRED)
    @Email(message = INVALID_EMAIL)
    private String senderEmail;

    @Getter
    @Setter
    private String status;

}
