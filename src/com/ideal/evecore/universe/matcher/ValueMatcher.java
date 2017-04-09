package com.ideal.evecore.universe.matcher;

import com.ideal.evecore.interpreter.data.EveObject;

/**
 * Created by Christophe on 08/04/2017.
 */
public interface ValueMatcher {
    boolean matches(EveObject o);
}
