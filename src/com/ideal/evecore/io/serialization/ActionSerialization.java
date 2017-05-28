package com.ideal.evecore.io.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rokuan.calliopecore.sentence.IAction;

import java.io.IOException;

/**
 * Created by Christophe on 28/05/2017.
 */
public class ActionSerialization {
    public static class ActionSerializer extends JsonSerializer<IAction> {
        @Override
        public void serialize(IAction action, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            boolean fieldBound = action.isFieldBound();
            boolean stateBound = action.isStateBound();
            IAction.ActionType actionValue = action.getAction();

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("action", actionValue != null ? actionValue.name() : null);
            jsonGenerator.writeBooleanField("is_field_bound", fieldBound);
            if (fieldBound) {
                jsonGenerator.writeStringField("field", action.getBoundField());
            }
            jsonGenerator.writeBooleanField("is_state_bound", stateBound);
            if (stateBound) {
                jsonGenerator.writeStringField("state", action.getBoundState());
                jsonGenerator.writeStringField("state_value", action.getState());
            }
            jsonGenerator.writeEndObject();
        }
    }

    public static class ActionDeserializer extends JsonDeserializer<IAction> {
        @Override
        public IAction deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            if (jsonParser.isExpectedStartObjectToken()) {
                jsonParser.nextFieldName(); // ACTION
                String action = jsonParser.nextTextValue();
                final IAction.ActionType actionValue = action != null ? IAction.ActionType.valueOf(action) : null;
                jsonParser.nextFieldName(); // IS_FIELD
                boolean fieldBound = jsonParser.nextBooleanValue();
                String field = null;
                if (fieldBound) {
                    jsonParser.nextFieldName(); // FIELD
                    field = jsonParser.nextTextValue();
                }
                jsonParser.nextFieldName(); // IS_STATE
                boolean stateBound = jsonParser.nextBooleanValue();
                String state = null;
                String stateValue = null;
                if (stateBound) {
                    jsonParser.nextFieldName(); // STATE
                    state = jsonParser.nextTextValue();
                    jsonParser.nextFieldName(); // STATE_VALUE
                    stateValue = jsonParser.nextTextValue();
                }
                return new Action(actionValue, fieldBound, field, stateBound, state, stateValue);
            }
            return null;
        }
    }

    private static class Action implements IAction {
        private final ActionType action;
        private final boolean fieldBound;
        private final String field;
        private final boolean stateBound;
        private final String state;
        private final String stateValue;

        public Action(ActionType a, boolean fBound, String f, boolean sBound, String s, String sValue) {
            action = a;
            fieldBound = fBound;
            field = f;
            stateBound = sBound;
            state = s;
            stateValue = sValue;
        }

        @Override
        public ActionType getAction() {
            return action;
        }

        @Override
        public Form getForm() {
            return null;
        }

        @Override
        public Tense getTense() {
            return null;
        }

        @Override
        public boolean isFieldBound() {
            return fieldBound;
        }

        @Override
        public String getBoundField() {
            return field;
        }

        @Override
        public boolean isStateBound() {
            return stateBound;
        }

        @Override
        public String getBoundState() {
            return state;
        }

        @Override
        public String getState() {
            return state;
        }

        @Override
        public String getValue() {
            return stateValue;
        }
    }
}
