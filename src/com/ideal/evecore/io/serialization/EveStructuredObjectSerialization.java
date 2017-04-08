package com.ideal.evecore.io.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.ideal.evecore.interpreter.data.EveStructuredObject;

import java.io.IOException;

/**
 * Created by Christophe on 08/04/2017.
 */
public class EveStructuredObjectSerialization {
    public static class EveStructuredObjectDeserializer extends StdDeserializer<EveStructuredObject> {
        public EveStructuredObjectDeserializer() {
            super(EveStructuredObject.class);
        }

        @Override
        public EveStructuredObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return null;
        }
    }
}
