package com.ideal.evecore.interpreter.data;

import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.util.Pair;

/**
 * Created by Christophe on 06/04/2017.
 */
public class EveQueryMappingObject extends EveMappingObject implements EveQueryObject {
    private final String id;

    public EveQueryMappingObject(String i, Mapping<EveObject> m) {
        super(m);
        id = i;
    }

    public EveQueryMappingObject(String i, Pair<String, EveObject>... ps) {
        super(ps);
        id = i;
    }

    @Override
    public String getId() {
        return id;
    }
}
