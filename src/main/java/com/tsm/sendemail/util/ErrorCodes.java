package com.tsm.sendemail.util;

public interface ErrorCodes {

    // GENERAL
    public static final String FIELD_REQUIRED = "field.required";

    public static final String INVALID_EMAIL = "invalidEmail";

    public static final String INVALID_STATUS = "invalidStatus";

    // CLIENT

    public static final String INVALID_NAME_SIZE = "invalidNameSize";

    public static final String INVALID_TOKEN_SIZE = "invalidTokenSize";

    // MESSAGE

    public static final String CLIENT_NOT_FOUND = "clientNotFound";

    public static final String INVALID_MESSAGE_SIZE = "invalidMessageSize";

    public static final String INVALID_SUBJECT_SIZE = "invalidSubjectSize";

    public static final String INVALID_SENDER_NAME_SIZE = "invalidSenderNameSize";

    public static final String INVALID_SENDER_EMAIL_SIZE = "invalidSenderEmailSize";

    public static final String INVALID_HOST = "invalidHost";

    public static final String ERROR_SENDING_EMAIL = "errorSendingEmail";

}
