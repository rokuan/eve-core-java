package com.ideal.evecore.io.command.query;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class FindItemByIdCommand extends AbstractCommand implements QueryCommand {
    private String id;

    protected FindItemByIdCommand() {
        super(FIND_ITEM_BY_ID);
    }
}
