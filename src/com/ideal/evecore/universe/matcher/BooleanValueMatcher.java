package com.ideal.evecore.universe.matcher;

import com.ideal.evecore.interpreter.data.EveBooleanObject;
import com.ideal.evecore.interpreter.data.EveObject;

/**
 * Created by Christophe on 09/04/2017.
 */
public class BooleanValueMatcher implements ValueMatcher {
    private final boolean value;

    public BooleanValueMatcher(boolean b) {
        value = b;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean matches(EveObject o) {
        if (o instanceof EveBooleanObject) {
            return value == ((EveBooleanObject) o).getValue();
        }
        return false;
    }
}
