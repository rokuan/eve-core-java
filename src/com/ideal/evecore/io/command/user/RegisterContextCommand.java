package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class RegisterContextCommand extends AbstractCommand implements UserCommand {
    protected RegisterContextCommand() {
        super(REGISTER_CONTEXT);
    }

    @JsonCreator
    protected static RegisterContextCommand getInstance() {
        return REGISTER_CONTEXT_COMMAND;
    }

    public static final RegisterContextCommand REGISTER_CONTEXT_COMMAND = new RegisterContextCommand();
}
