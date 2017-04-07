package com.ideal.evecore.common;

/**
 * Created by chris on 07/04/2017.
 */
public final class Credentials {
    private String login;
    private String password;

    public Credentials(String l, String p) {
        login = l;
        password = p;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
