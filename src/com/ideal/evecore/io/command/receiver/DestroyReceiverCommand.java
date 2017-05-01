package com.ideal.evecore.io.command.receiver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class DestroyReceiverCommand extends AbstractCommand implements ReceiverCommand {
    protected DestroyReceiverCommand() {
        super(DESTROY_RECEIVER);
    }

    @JsonCreator
    protected static DestroyReceiverCommand getInstance() {
        return DESTROY_RECEIVER_COMMAND;
    }

    public static final DestroyReceiverCommand DESTROY_RECEIVER_COMMAND = new DestroyReceiverCommand();
}
