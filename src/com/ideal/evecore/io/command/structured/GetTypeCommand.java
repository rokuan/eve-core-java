package com.ideal.evecore.io.command.structured;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class GetTypeCommand extends AbstractCommand implements EveStructuredObjectCommand {
    protected GetTypeCommand() {
        super(GET_TYPE);
    }

    @JsonCreator
    protected static GetTypeCommand getInstance() {
        return GET_TYPE_COMMAND;
    }

    public static final GetTypeCommand GET_TYPE_COMMAND = new GetTypeCommand();
}
