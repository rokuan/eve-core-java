package com.ideal.evecore.io.command.structured;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class SetStateCommand extends AbstractCommand implements EveStructuredObjectCommand {
    private final String field;
    private final String value;

    @JsonCreator
    public SetStateCommand(@JsonProperty("field") String f, @JsonProperty("value") String v) {
        super(SET_STATE);
        field = f;
        value = v;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
