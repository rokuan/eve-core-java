package com.ideal.evecore.io.command.structured;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class SetFieldCommand extends AbstractCommand implements EveStructuredObjectCommand {
    private final String field;
    private final EveObject value;

    @JsonCreator
    public SetFieldCommand(@JsonProperty("field") String f, @JsonProperty("value") EveObject v) {
        super(SET_FIELD);
        field = f;
        value = v;
    }

    public String getField() {
        return field;
    }

    public EveObject getValue() {
        return value;
    }
}