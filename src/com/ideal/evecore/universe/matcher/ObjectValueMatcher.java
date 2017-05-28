package com.ideal.evecore.universe.matcher;

import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christophe on 09/04/2017.
 */
public class ObjectValueMatcher implements ValueMatcher {
    private final Map<String, ValueMatcher> values;

    public ObjectValueMatcher(Map<String, ValueMatcher> ms) {
        values = ms;
    }

    public ObjectValueMatcher(Pair<String, ValueMatcher>... ms) {
        values = new HashMap<String, ValueMatcher>();
        for (Pair<String, ValueMatcher> entry : ms) {
            values.put(entry.first, entry.second);
        }
    }

    public Map<String, ValueMatcher> getValues() {
        return values;
    }

    @Override
    public boolean matches(EveObject o) {
        if (o instanceof EveStructuredObject) {
            EveStructuredObject structuredObject = (EveStructuredObject) o;
            for (Map.Entry<String, ValueMatcher> entry : values.entrySet()) {
                Option<EveObject> value = structuredObject.get(entry.getKey());
                if (value.isEmpty() || !entry.getValue().matches(value.get())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
