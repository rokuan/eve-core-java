package com.ideal.evecore.interpreter.data;

import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.util.Matcher;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Pair;
import com.ideal.evecore.util.Transformer;
import com.rokuan.calliopecore.sentence.IAction;

/**
 * Created by Christophe on 06/04/2017.
 */
public class EveMappingObject implements EveStructuredObject {
    private final Mapping<EveObject> mapping;

    public EveMappingObject(Mapping<EveObject> m) {
        mapping = m;
    }

    public EveMappingObject(Pair<String, EveObject>... ps) {
        Mapping<EveObject> m = new Mapping<EveObject>();
        for (Pair<String, EveObject> pair : ps) {
            m.put(pair.first, pair.second);
        }
        mapping = m;
    }

    public Mapping<EveObject> getValues() {
        return mapping;
    }

    @Override
    public String getType() {
        return get(EveObject.TYPE_KEY).gather(new Matcher.SimpleMatcher<EveStringObject, String>(EveStringObject.class, new Transformer<EveStringObject, String>() {
            @Override
            public String apply(EveStringObject eveStringObject) {
                return eveStringObject.getValue();
            }
        })).getOrElse("");
    }

    @Override
    public boolean has(String field) {
        return mapping.containsKey(field);
    }

    @Override
    public boolean hasState(String state) {
        return false;
    }

    @Override
    public Option<EveObject> get(String field) {
        return Option.apply(mapping.get(field));
    }

    @Override
    public Option<String> getState(String state) {
        return Option.empty();
    }

    @Override
    public boolean set(String field, EveObject value) {
        return false;
    }

    @Override
    public boolean setState(String field, String value) {
        return false;
    }

    @Override
    public boolean call(IAction action) {
        return false;
    }
}
