package com.ideal.evecore.interpreter;

import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveObjectList;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.util.Option;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Christophe on 22/04/2017.
 */
public class Environment implements Context {
    private final List<Context> contexts = new ArrayList<Context>();

    @Override
    public Option<EveStructuredObject> findOneItemOfType(String type) {
        synchronized (contexts) {
            for (Context c: contexts) {
                Option<EveStructuredObject> result = c.findOneItemOfType(type);
                if (result.isDefined()) {
                    return result;
                }
            }
            return Option.empty();
        }
    }

    @Override
    public Option<EveObjectList> findItemsOfType(String type) {
        synchronized (contexts) {
            List<EveObject> results = new LinkedList<EveObject>();

            for (Context c : contexts) {
                Option<EveObjectList> items = c.findItemsOfType(type);
                if (items.isDefined()) {
                    results.addAll(items.get().getValues());
                }
            }

            if (results.isEmpty()) {
                return Option.empty();
            } else {
                return new Option.Some(new EveObjectList(results));
            }
        }
    }

    public void addContext(Context c) {
        contexts.add(c);
    }

    public void remoteContext(Context c) {
        contexts.remove(c);
    }
}
