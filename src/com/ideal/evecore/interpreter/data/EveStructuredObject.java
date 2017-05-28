package com.ideal.evecore.interpreter.data;

import com.ideal.evecore.util.Option;
import com.rokuan.calliopecore.sentence.IAction;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface EveStructuredObject extends EveObject {
    String getType();

    boolean has(String field);

    boolean hasState(String state);

    Option<EveObject> get(String field);

    Option<String> getState(String state);

    boolean set(String field, EveObject value);

    boolean setState(String field, String value);

    boolean call(IAction action);
}
