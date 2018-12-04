package com.tsm.sendemail.util;

public abstract class ErrorCodes {

    // GENERAL
    public static final String REQUIRED_EMAIL = "requiredEmail";

    public static final String INVALID_EMAIL = "invalidEmail";

    public static final String INVALID_STATUS = "invalidClientStatus";

    // CLIENT
    public static final String REQUIRED_NAME = "requiredName";

    public static final String INVALID_NAME_SIZE = "invalidNameSize";

    public static final String REQUIRED_TOKEN = "requiredToken";

    public static final String REQUIRED_HOSTS = "requiredHosts";

    public static final String REQUIRED_EMAIL_RECIPIENT = "requiredEmailRecipient";

    public static final String INVALID_TOKEN_SIZE = "invalidTokenSize";

    public static final String DUPLICATED_TOKEN = "duplicatedToken";

    // MESSAGE
    public static final String CLIENT_NOT_FOUND = "clientNotFound";

    public static final String REQUIRED_MESSAGE = "requiredMessage";

    public static final String INVALID_MESSAGE_SIZE = "invalidMessageSize";

    public static final String REQUIRED_SUBJECT = "requiredSubject";

    public static final String INVALID_SUBJECT_SIZE = "invalidSubjectSize";

    public static final String REQUIRED_SENDER_NAME = "requiredSenderName";

    public static final String INVALID_SENDER_NAME_SIZE = "invalidSenderNameSize";

    public static final String REQUIRED_SENDER_EMAIL = "requiredSenderEmail";

    public static final String INVALID_HOST = "invalidHost";

    public static final String ERROR_SENDING_EMAIL = "errorSendingEmail";

    public static final String MESSAGE_NOT_FOUND = "messageNotFound";

    public static final String REPORT_NOT_SENT = "reportNotSent";

    public static final String INVALID_SEARCH_PARAMS = "invalidSearchParams";

    // LOGIN
    public static final String REQUIRED_LOGIN_EMAIL = "requiredLoginEmail";

    public static final String INVALID_LOGIN_EMAIL = "invalidLoginEmail";

    public static final String REQUIRED_LOGIN_PASS = "requiredLoginPassword";

}
