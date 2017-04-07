package com.ideal.evecore.interpreter;

import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.util.Option;

/**
 * Created by chris on 07/04/2017.
 */
public interface QuerySource {
    public Option<EveStructuredObject> findById(String id);
}