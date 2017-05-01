package com.ideal.evecore.io.command.receiver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class GetMappingsCommand extends AbstractCommand implements ReceiverCommand {
    protected GetMappingsCommand() {
        super(GET_MAPPINGS);
    }

    @JsonCreator
    protected static GetMappingsCommand getInstance() {
        return GET_MAPPINGS_COMMAND;
    }

    public static final GetMappingsCommand GET_MAPPINGS_COMMAND = new GetMappingsCommand();
}
