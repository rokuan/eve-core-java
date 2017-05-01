package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */

public final class RegisterReceiverCommand extends AbstractCommand implements UserCommand {
    protected RegisterReceiverCommand() {
        super(REGISTER_RECEIVER);
    }

    @JsonCreator
    protected static RegisterReceiverCommand getInstance() {
        return REGISTER_RECEIVER_COMMAND;
    }

    public static final RegisterReceiverCommand REGISTER_RECEIVER_COMMAND = new RegisterReceiverCommand();
}