package com.ideal.evecore.interpreter.data;

import com.ideal.evecore.common.Mapping;

/**
 * Created by chris on 06/04/2017.
 */
public class EveQueryMappingObject extends EveMappingObject implements EveQueryObject {
    private String id;

    public EveQueryMappingObject(String i, Mapping<EveObject> m) {
        super(m);
        id = i;
    }

    @Override
    public String getId() {
        return id;
    }
}
