package com.ideal.evecore.io.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ideal.evecore.universe.matcher.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christophe on 09/04/2017.
 */
public class ValueMatcherSerialization {
    public static class ValueMatcherSerializer extends JsonSerializer<ValueMatcher> {
        @Override
        public void serialize(ValueMatcher matcher, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            if (matcher instanceof StringValueMatcher) {
                jsonGenerator.writeString(((StringValueMatcher) matcher).getValue());
            } else if (matcher instanceof NumberValueMatcher) {
                jsonGenerator.writeNumber(((NumberValueMatcher) matcher).getValue().doubleValue());
            } else if (matcher instanceof AnyValueMatcher) {
                jsonGenerator.writeString("*");
            } else if (matcher instanceof OrValueMatcher) {
                OrValueMatcher orMatcher = (OrValueMatcher) matcher;
                jsonGenerator.writeStartArray();
                for (ValueMatcher m : orMatcher.getValues()) {
                    serialize(m, jsonGenerator, serializerProvider);
                }
                jsonGenerator.writeEndArray();
            } else if (matcher instanceof BooleanValueMatcher) {
                jsonGenerator.writeBoolean(((BooleanValueMatcher) matcher).getValue());
            } else if (matcher instanceof ObjectValueMatcher) {
                ObjectValueMatcher objectMatcher = (ObjectValueMatcher) matcher;
                jsonGenerator.writeStartObject();
                for (Map.Entry<String, ValueMatcher> entry: objectMatcher.getValues().entrySet()) {
                    jsonGenerator.writeFieldName(entry.getKey());
                    serialize(entry.getValue(), jsonGenerator, serializerProvider);
                }
                jsonGenerator.writeEndObject();
            } else if (matcher instanceof NullValueMatcher) {
                jsonGenerator.writeNull();
            } else {
                // TODO: UndefinedValueMatcher ?
            }
        }
    }

    public static class ValueMatcherDeserializer extends JsonDeserializer<ValueMatcher> {
        @Override
        public ValueMatcher deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            switch (jsonParser.getCurrentToken()) {
                case VALUE_STRING:
                    String value = jsonParser.getText();
                    if ("*".equals(value)) {
                        return new AnyValueMatcher();
                    } else {
                        return new StringValueMatcher(value);
                    }
                case VALUE_TRUE:
                case VALUE_FALSE:
                    return new BooleanValueMatcher(jsonParser.getBooleanValue());
                case VALUE_NUMBER_INT:
                    return new NumberValueMatcher(jsonParser.getIntValue());
                case VALUE_NUMBER_FLOAT:
                    return new NumberValueMatcher(jsonParser.getDoubleValue());
                case START_ARRAY:
                    List<ValueMatcher> matchers = new ArrayList<ValueMatcher>();
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        matchers.add(deserialize(jsonParser, deserializationContext));
                    }
                    return new OrValueMatcher(matchers);
                case START_OBJECT:
                    Map<String, ValueMatcher> entries = new HashMap<String, ValueMatcher>();
                    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                        entries.put(jsonParser.nextFieldName(), deserialize(jsonParser, deserializationContext));
                    }
                    return new ObjectValueMatcher(entries);
                case VALUE_NULL:
                    return new NullValueMatcher();
                default:
                    return new UndefinedValueMatcher();
            }
        }
    }
}
