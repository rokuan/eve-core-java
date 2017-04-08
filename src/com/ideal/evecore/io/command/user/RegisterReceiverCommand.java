package com.ideal.evecore.io.command.user;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */

public final class RegisterReceiverCommand extends AbstractCommand implements UserCommand {
    public RegisterReceiverCommand() {
        super(REGISTER_RECEIVER);
    }
}