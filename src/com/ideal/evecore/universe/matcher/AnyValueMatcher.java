package com.ideal.evecore.universe.matcher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.interpreter.data.EveObject;

/**
 * Created by Christophe on 09/04/2017.
 */
public class AnyValueMatcher implements ValueMatcher {
    protected AnyValueMatcher() {

    }

    @Override
    public boolean matches(EveObject o) {
        return true;
    }

    @JsonCreator
    protected static AnyValueMatcher getInstance() {
        return ANY_VALUE_MATCHER;
    }

    public static final AnyValueMatcher ANY_VALUE_MATCHER = new AnyValueMatcher();
}
