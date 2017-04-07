package com.ideal.evecore.io.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        private final String objectId;
        private final EveStructuredObjectCommand objectCommand;

        @JsonCreator
        public ObjectCommand(@JsonProperty("objectId") String id, @JsonProperty("objectCommand") EveStructuredObjectCommand cmd) {
            super(OBJECT_REQUEST);
            objectId = id;
            objectCommand = cmd;
        }

        public String getObjectId() {
            return objectId;
        }

        public EveStructuredObjectCommand getObjectCommand() {
            return objectCommand;
        }
    }
}
