package com.ideal.evecore.interpreter;

import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.util.Option;

/**
 * Created by Christophe on 07/04/2017.
 */
public interface QuerySource {
    /**
     * Finds an item by its id
     * @param id The object's id
     * @return The object with this id
     */
    public Option<EveStructuredObject> findById(String id);
}
