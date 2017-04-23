package com.ideal.evecore.io.command.receiver;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class DestroyReceiverCommand extends AbstractCommand implements ReceiverCommand {
    public DestroyReceiverCommand() {
        super(DESTROY_RECEIVER);
    }

    public static final DestroyReceiverCommand DESTROY_RECEIVER_COMMAND = new DestroyReceiverCommand();
}
