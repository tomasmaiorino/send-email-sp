package com.tsm.sendemail.resources;

import static com.tsm.sendemail.util.ErrorCodes.INVALID_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_MESSAGE_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_SENDER_NAME_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.INVALID_SUBJECT_SIZE;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_MESSAGE;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_SENDER_EMAIL;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_SENDER_NAME;
import static com.tsm.sendemail.util.ErrorCodes.REQUIRED_SUBJECT;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tsm.sendemail.model.Message;

import lombok.Getter;
import lombok.Setter;

public class MessageResource implements BaseResource {

    public static final String EMAIL = "^\\w+([-+._]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_MESSAGE)
    @Size(min = 2, max = Message.MESSAGE_MAX_LENGTH, message = INVALID_MESSAGE_SIZE)
    private String message;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_SUBJECT)
    @Size(min = 2, max = 30, message = INVALID_SUBJECT_SIZE)
    private String subject;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_SENDER_NAME)
    @Size(min = 2, max = 30, message = INVALID_SENDER_NAME_SIZE)
    private String senderName;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_SENDER_EMAIL)
    @Pattern(regexp = EMAIL, message = INVALID_EMAIL)
    private String senderEmail;

    @Getter
    @Setter
    private String status;

}
