package com.ideal.evecore.io.command;

import com.ideal.evecore.interpreter.Context;

/**
 * Created by chris on 06/04/2017.
 */
public interface QueryCommand extends ContextCommand, ReceiverCommand {
    public static final String FIND_ITEM_BY_ID = "FBID";
    public static final String OBJECT_REQUEST = "ORQT";

    public static class FindItemByIdCommand extends AbstractCommand implements QueryCommand {
        private String id;

        protected FindItemByIdCommand() {
            super(FIND_ITEM_BY_ID);
        }
    }

    public static class ObjectCommand extends AbstractCommand implements QueryCommand {
        protected ObjectCommand() {
            super(OBJECT_REQUEST);
        }
    }
}
