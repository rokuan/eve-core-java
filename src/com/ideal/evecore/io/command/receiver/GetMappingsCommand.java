package com.ideal.evecore.io.command.receiver;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class GetMappingsCommand extends AbstractCommand implements ReceiverCommand {
    public GetMappingsCommand() {
        super(GET_MAPPINGS);
    }

    public static final GetMappingsCommand GET_MAPPINGS_COMMAND = new GetMappingsCommand();
}
