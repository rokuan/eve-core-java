package com.ideal.evecore.io.command.receiver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class GetReceiverNameCommand extends AbstractCommand implements ReceiverCommand {
    protected GetReceiverNameCommand() {
        super(GET_RECEIVER_NAME);
    }

    @JsonCreator
    protected static GetReceiverNameCommand getInstance() {
        return GET_RECEIVER_NAME_COMMAND;
    }

    public static final GetReceiverNameCommand GET_RECEIVER_NAME_COMMAND = new GetReceiverNameCommand();
}
