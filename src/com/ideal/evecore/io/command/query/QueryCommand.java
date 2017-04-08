package com.ideal.evecore.io.command.query;

import com.ideal.evecore.io.command.receiver.ReceiverCommand;
import com.ideal.evecore.io.command.context.ContextCommand;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface QueryCommand extends ContextCommand, ReceiverCommand {
    public static final String FIND_ITEM_BY_ID = "FBID";
    public static final String OBJECT_REQUEST = "ORQT";
}
