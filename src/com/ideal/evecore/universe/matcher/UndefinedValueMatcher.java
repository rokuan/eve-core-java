package com.ideal.evecore.universe.matcher;

import com.ideal.evecore.interpreter.data.EveObject;

/**
 * Created by Christophe on 09/04/2017.
 */
public class UndefinedValueMatcher implements ValueMatcher {
    @Override
    public boolean matches(EveObject o) {
        return false;
    }

    public static final UndefinedValueMatcher UNDEFINED_VALUE_MATCHER = new UndefinedValueMatcher();
}
