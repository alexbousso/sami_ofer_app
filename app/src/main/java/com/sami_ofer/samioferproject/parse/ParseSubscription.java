package com.sami_ofer.samioferproject.parse;

import android.content.Context;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex on 03/12/15.
 */
@ParseClassName("Subscription")
public class ParseSubscription extends ParseObject {
    private static final String EMAIL_FIELD = "email";

    public String getEmail() {
        return getString(EMAIL_FIELD);
    }

    public boolean setEmail(String email) {
        if(!checkIfValidEmail(email))
            return false;

        put(EMAIL_FIELD, email);
        return true;
    }

    private boolean checkIfValidEmail(String email) {
        // TODO: add some tests
//        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Pattern p = Pattern.compile("[^@]+@[^@]+\\.[^@]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static ParseQuery<ParseSubscription> fetchCurrentQuery(String email) {
        return ParseQuery.getQuery(ParseSubscription.class)
                .whereEqualTo(EMAIL_FIELD, email);
    }
}
