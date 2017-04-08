package com.ideal.evecore.io.command.structured;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class HasStateCommand extends AbstractCommand implements EveStructuredObjectCommand {
    private final String field;

    @JsonCreator
    public HasStateCommand(@JsonProperty("field") String f) {
        super(HAS_STATE);
        field = f;
    }

    public String getField() {
        return field;
    }
}
