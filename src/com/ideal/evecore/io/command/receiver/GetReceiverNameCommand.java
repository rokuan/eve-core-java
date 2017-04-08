package com.ideal.evecore.io.command.receiver;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class GetReceiverNameCommand extends AbstractCommand implements ReceiverCommand {
    public GetReceiverNameCommand() {
        super(GET_RECEIVER_NAME);
    }
}
