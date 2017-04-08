package com.ideal.evecore.io.command.structured;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class GetTypeCommand extends AbstractCommand implements EveStructuredObjectCommand {
    public GetTypeCommand() {
        super(GET_TYPE);
    }
}
