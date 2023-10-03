package com.ba.demo.core.utils;

import lombok.Getter;

@Getter
public class MessagesKey {
    private final String name;
    private final int numTokens;

    private MessagesKey(String key, int numTokens){
        this.name = key;
        this.numTokens = numTokens;
    }

    public static final MessagesKey entity_not_found = new MessagesKey("entity_not_found",1);
    public static final MessagesKey bad_credentials = new MessagesKey("bad_credentials",0);
    public static final MessagesKey bad_request = new MessagesKey("bad_request",1);
    public static final MessagesKey expired_token = new MessagesKey("expired_token",1);
    public static final MessagesKey username_already_exists = new MessagesKey("username_already_exists",0);
    public static final MessagesKey email_already_exists = new MessagesKey("email_already_exists",0);
    public static final MessagesKey user_already_exists = new MessagesKey("user_already_exists",0);
    public static final MessagesKey bad_username = new MessagesKey("bad_username",0);
    public static final MessagesKey require_authentication = new MessagesKey("require_authentication",0);
    public static final MessagesKey user_not_found = new MessagesKey("user_not_found",0);
    public static final MessagesKey account_not_verified = new MessagesKey("account_not_verified",0);
    public static final MessagesKey account_not_active = new MessagesKey("account_not_active",0);
    public static final MessagesKey invalid_token = new MessagesKey("invalid_token",0);
}
