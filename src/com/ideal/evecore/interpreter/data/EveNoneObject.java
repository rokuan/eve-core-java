package com.ideal.evecore.interpreter.data;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by Christophe on 21/04/2017.
 */
public class EveNoneObject implements EveObject {
    private EveNoneObject() {

    }

    @JsonCreator
    protected static EveNoneObject getInstance() {
        return NONE;
    }

    public static final String NONE_LABEL = "[__EveNone][/__EveNone]";
    public static final EveNoneObject NONE = new EveNoneObject();
}
