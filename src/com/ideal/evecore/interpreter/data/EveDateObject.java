package com.ideal.evecore.interpreter.data;

import java.util.Date;

/**
 * Created by chris on 21/04/2017.
 */
public class EveDateObject implements EveObject {
    private final Date value;

    public EveDateObject(Date d) {
        value = d;
    }

    public Date getValue() {
        return value;
    }
}
