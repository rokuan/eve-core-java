package com.ideal.evecore.interpreter;

import com.ideal.evecore.interpreter.data.EveObjectList;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.util.Option;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface Context {
    /**
     * Finds a single item that matches the type in this context
     * @param type The type to query
     * @return An option containing the result if any
     */
    Option<EveStructuredObject> findOneItemOfType(String type);

    /**
     * Queries all the items that match the type in this context
     * @param type The type to query
     * @return An option containing the results if any
     */
    Option<EveObjectList> findItemsOfType(String type);
}
