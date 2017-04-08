package com.ideal.evecore.interpreter;

import com.ideal.evecore.interpreter.data.EveObjectList;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.util.Option;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface Context {
    Option<EveStructuredObject> findOneItemOfType(String type);
    Option<EveObjectList> findItemsOfType(String type);
}
