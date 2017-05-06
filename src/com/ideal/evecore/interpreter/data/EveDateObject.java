package com.ideal.evecore.interpreter.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Christophe on 21/04/2017.
 */
public class EveDateObject implements EveObject {
    private final Date value;

    public EveDateObject(Date d) {
        value = d;
    }

    public Date getValue() {
        return value;
    }

    static public class EveDateObjectPlaceHolder {
        private static final String FORMAT = "[__EveDate]%s[/__EveDate]";
        private static final String DATE_FORMAT_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} [+-][0-9]{4}";
        private static final String EVE_DATE_REGEX = "\\[__EveDate\\](" + DATE_FORMAT_REGEX + ")\\[/__EveDate\\]";

        public boolean matches(String value) {
            return value.matches(EVE_DATE_REGEX);
        }

        public String format(Date value) {
            return String.format(FORMAT, DATE_FORMAT.format(value));
        }

        public Date getValue(String value) {
            Pattern eveDatePattern = Pattern.compile(EVE_DATE_REGEX);
            Matcher matcher = eveDatePattern.matcher(value);
            if (matcher.matches()) {
                String dateString = matcher.group(1);
                try {
                    return DATE_FORMAT.parse(dateString);
                } catch (ParseException e) {
                    // TODO: handle the error ?
                    e.printStackTrace();
                    return new Date();
                }
            } else {
                // TODO: handle this case
                return new Date();
            }
        }
    }

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    public static final EveDateObjectPlaceHolder PLACE_HOLDER = new EveDateObjectPlaceHolder();
}
