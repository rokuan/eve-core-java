package com.ideal.evecore.io.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;

import java.io.IOException;

/**
 * Created by Christophe on 08/04/2017.
 */
public class EveStructuredObjectSerialization {
    public static class EveStructuredObjectSerializer extends JsonSerializer<EveStructuredObject> {
        private final EveObjectSerialization.EveObjectSerializer objectSerializer;

        public EveStructuredObjectSerializer(String dId) {
            objectSerializer = new EveObjectSerialization.EveObjectSerializer(dId);
        }

        @Override
        public void serialize(EveStructuredObject eveStructuredObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            objectSerializer.serialize(eveStructuredObject, jsonGenerator, serializerProvider);
        }
    }

    public static class EveStructuredObjectDeserializer extends JsonDeserializer<EveStructuredObject> {
        private final EveObjectSerialization.EveObjectDeserializer objectDeserializer;

        public EveStructuredObjectDeserializer(StreamHandler h) {
            objectDeserializer = new EveObjectSerialization.EveObjectDeserializer(h);
        }

        @Override
        public EveStructuredObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return (EveStructuredObject) objectDeserializer.deserialize(jsonParser, deserializationContext);
        }
    }
}
