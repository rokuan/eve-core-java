package com.ideal.evecore.io.command;

/**
 * Created by chris on 06/04/2017.
 */
public interface ContextCommand {
    public static final String FIND_ITEMS_OF_TYPE = "FTYP";
    public static final String FIND_ONE_ITEM_OF_TYPE = "FOTY";

    public static class FindOneItemOfTypeCommand extends AbstractCommand implements ContextCommand {
        private String itemType;

        protected FindOneItemOfTypeCommand() {
            super(FIND_ONE_ITEM_OF_TYPE);
        }
    }

    public static class FindItemsOfTypeCommand extends AbstractCommand implements ContextCommand {
        private String itemType;

        protected FindItemsOfTypeCommand() {
            super(FIND_ITEMS_OF_TYPE);
        }
    }
}
