package com.example.arttree.app;

import java.util.regex.Pattern;

public class RegExp {
    private String idRegExp = "^[a-zA-Z0-9_]{4,20}$";
    private String passRegExp = "^[a-zA-Z0-9_]{4,20}$";

    public boolean isIdMatches(String id) {
        return Pattern.matches(idRegExp, id);
    }

    public boolean isPasswordMatches(String pass) {
        return Pattern.matches(passRegExp, pass);
    }
}
