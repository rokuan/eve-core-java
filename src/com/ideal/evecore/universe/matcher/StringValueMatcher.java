package com.ideal.evecore.universe.matcher;

import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStringObject;

/**
 * Created by Christophe on 09/04/2017.
 */
public class StringValueMatcher implements ValueMatcher {
    private final String value;

    public StringValueMatcher(String s) {
        value = s;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean matches(EveObject o) {
        if (o instanceof EveStringObject) {
            return value.equals(((EveStringObject) o).getValue());
        }
        return false;
    }
}
