package com.ideal.evecore.io.command.receiver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class InitReceiverCommand extends AbstractCommand implements ReceiverCommand {
    protected InitReceiverCommand() {
        super(INIT_RECEIVER);
    }

    @JsonCreator
    protected static InitReceiverCommand getInstance() {
        return INIT_RECEIVER_COMMAND;
    }

    public static final InitReceiverCommand INIT_RECEIVER_COMMAND = new InitReceiverCommand();
}
