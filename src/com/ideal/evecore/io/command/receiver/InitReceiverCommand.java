package com.ideal.evecore.io.command.receiver;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class InitReceiverCommand extends AbstractCommand implements ReceiverCommand {
    public InitReceiverCommand() {
        super(INIT_RECEIVER);
    }

    public static final InitReceiverCommand INIT_RECEIVER_COMMAND = new InitReceiverCommand();
}
