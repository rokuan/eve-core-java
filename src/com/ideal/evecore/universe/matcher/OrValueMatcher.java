package com.ideal.evecore.universe.matcher;

import com.ideal.evecore.interpreter.data.EveObject;

import java.util.List;

/**
 * Created by Christophe on 09/04/2017.
 */
public class OrValueMatcher implements ValueMatcher {
    private final ValueMatcher[] values;

    public OrValueMatcher(ValueMatcher... ms) {
        values = ms;
    }

    public OrValueMatcher(List<ValueMatcher> ms) {
        values = ms.toArray(new ValueMatcher[0]);
    }

    public ValueMatcher[] getValues() {
        return values;
    }

    @Override
    public boolean matches(EveObject o) {
        for (int i=0; i<values.length; i++) {
            if (values[i].matches(o)) {
                return true;
            }
        }
        return false;
    }
}
