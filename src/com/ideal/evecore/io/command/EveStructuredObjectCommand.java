package com.ideal.evecore.io.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.interpreter.data.EveObject;

/**
 * Created by chris on 06/04/2017.
 */
public interface EveStructuredObjectCommand {
    public static final String GET_TYPE = "GTYP";
    public static final String SET_FIELD = "SFLD";
    public static final String GET_FIELD = "GFLD";
    public static final String HAS_FIELD = "HFLD";
    public static final String GET_STATE = "GSTE";
    public static final String SET_STATE = "SSTE";
    public static final String HAS_STATE = "HSTE";

    public static class GetTypeCommand extends AbstractCommand implements EveStructuredObjectCommand {
        public GetTypeCommand() {
            super(GET_TYPE);
        }
    }

    public static class GetFieldCommand extends AbstractCommand implements EveStructuredObjectCommand {
        private final String field;

        @JsonCreator
        public GetFieldCommand(@JsonProperty("field") String f) {
            super(GET_FIELD);
            field = f;
        }
    }

    public static class SetFieldCommand extends AbstractCommand implements EveStructuredObjectCommand {
        private final String field;
        private final EveObject value;

        @JsonCreator
        public SetFieldCommand(@JsonProperty("field") String f, @JsonProperty("value") EveObject v){
            super(SET_FIELD);
            field = f;
            value = v;
        }
    }

    public static class HasFieldCommand extends AbstractCommand implements EveStructuredObjectCommand {
        private final String field;

        @JsonCreator
        public HasFieldCommand(@JsonProperty("field") String f) {
            super(HAS_FIELD);
            field = f;
        }
    }

    public static class GetStateCommand extends AbstractCommand implements EveStructuredObjectCommand {
        private final String field;

        @JsonCreator
        public GetStateCommand(@JsonProperty("field") String f) {
            super(GET_STATE);
            field = f;
        }
    }

    public static class SetStateCommand extends AbstractCommand implements EveStructuredObjectCommand {
        private final String field;
        private final String value;

        @JsonCreator
        public SetStateCommand(@JsonProperty("field") String f, @JsonProperty("value") String v) {
            super(SET_STATE);
            field = f;
            value = v;
        }
    }

    public static final class HasStateCommand extends AbstractCommand implements EveStructuredObjectCommand {
        private final String field;

        @JsonCreator
        public HasStateCommand(@JsonProperty("field") String f) {
            super(HAS_STATE);
            field = f;
        }
    }
}
