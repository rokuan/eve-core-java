package com.ideal.evecore.interpreter.data;

import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.util.Matcher;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Transformer;

/**
 * Created by chris on 06/04/2017.
 */
public class EveMappingObject implements EveStructuredObject {
    private Mapping<EveObject> mapping;

    public EveMappingObject(Mapping<EveObject> m){
        mapping = m;
    }

    @Override
    public String getType() {
        return get(EveObject.TYPE_KEY).collect(new Matcher.SimpleMatcher<EveStringObject, String>(EveStringObject.class, new Transformer<EveStringObject, String>() {
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
    public void set(String field, EveObject value) {

    }

    @Override
    public void setState(String field, String value) {

    }
}
