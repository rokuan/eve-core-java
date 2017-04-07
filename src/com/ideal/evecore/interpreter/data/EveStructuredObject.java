package com.ideal.evecore.interpreter.data;

import com.ideal.evecore.util.Option;

/**
 * Created by chris on 06/04/2017.
 */
public interface EveStructuredObject extends EveObject {
    String getType();
    boolean has(String field);
    boolean hasState(String state);
    Option<EveObject> get(String field);
    Option<String> getState(String state);
    void set(String field, EveObject value);
    void setState(String field, String value);
}
