package com.ideal.evecore.universe.receiver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.interpreter.data.EveStructuredObject;

/**
 * Created by Christophe on 06/04/2017.
 */
public class EveObjectMessage {
    private EveStructuredObject content;

    @JsonCreator
    public EveObjectMessage(@JsonProperty("content") EveStructuredObject o) {
        content = o;
    }

    public EveStructuredObject getContent() {
        return content;
    }
}
