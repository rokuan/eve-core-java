package com.ideal.evecore.universe.matcher;

import com.ideal.evecore.interpreter.data.EveNumberObject;
import com.ideal.evecore.interpreter.data.EveObject;

/**
 * Created by Christophe on 09/04/2017.
 */
public class NumberValueMatcher implements ValueMatcher {
    private final Number value;

    public NumberValueMatcher(Number n) {
        value = n;
    }

    @Override
    public boolean matches(EveObject o) {
        if (o instanceof EveNumberObject) {
            return (value == ((EveNumberObject) o).getValue());
        }
        return false;
    }

    public Number getValue() {
        return value;
    }
}
