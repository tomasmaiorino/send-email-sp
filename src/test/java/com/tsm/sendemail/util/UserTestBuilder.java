package com.tsm.sendemail.util;

import com.tsm.sendemail.model.User;

public class UserTestBuilder extends BaseTestBuilder {


    public static final String LARGE_NAME = getLargeString(UserConstants.CUSTOMER_MAX_NAME_SIZE);
    public static final String SMALL_NAME = getSmallString(UserConstants.CUSTOMER_MIN_NAME_SIZE);
    public static final String LARGE_EMAIL = getLargeString(UserConstants.CUSTOMER_MAX_EMAIL_SIZE);
    public static final String SMALL_EMAIL = getSmallString(UserConstants.CUSTOMER_MIN_EMAIL_SIZE);
    public static final String LARGE_PASSWORD = getLargeString(UserConstants.CUSTOMER_MAX_PASSWORD_SIZE);
    public static final String SMALL_PASSWORD = getSmallString(UserConstants.CUSTOMER_MIN_PASSWORD_SIZE);

    public static User buildModel() {
        return buildModel(getName(), getValidEmail(), getPassword(), getCustomerStatus());
    }

    public static User buildModel(final String name, final String email, final String password,
                                  final User.UserStatus userStatus) {
        return User.UserBuilder.User(name, email, password, userStatus).build();
    }



    public static String getName() {
        return getString(UserConstants.CUSTOMER_MAX_EMAIL_SIZE);
    }

    public static String getPassword() {
        return getString(UserConstants.CUSTOMER_MAX_PASSWORD_SIZE);
    }

    public static String getCustomerStatusAsString() {
        return getCustomerStatus().name();
    }

    public static User.UserStatus getCustomerStatus() {
        return User.UserStatus.ACTIVE;
    }

}
