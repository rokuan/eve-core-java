package com.ideal.evecore.io.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveObjectList;
import com.ideal.evecore.io.StreamHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christophe on 09/04/2017.
 */
public class EveObjectListSerialization {
    public static class EveObjectListSerializer extends JsonSerializer<EveObjectList> {
        private final EveObjectSerialization.EveObjectSerializer objectSerializer;

        public EveObjectListSerializer(String dId) {
            objectSerializer = new EveObjectSerialization.EveObjectSerializer(dId);
        }

        @Override
        public void serialize(EveObjectList eveObjectList, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            objectSerializer.serialize(eveObjectList, jsonGenerator, serializerProvider);
        }
    }

    public static class EveObjectListDeserializer extends JsonDeserializer<EveObjectList> {
        private final EveObjectSerialization.EveObjectDeserializer objectDeserializer;

        public EveObjectListDeserializer(StreamHandler h) {
            objectDeserializer = new EveObjectSerialization.EveObjectDeserializer(h);
        }

        @Override
        public EveObjectList deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return (EveObjectList) objectDeserializer.deserialize(jsonParser, deserializationContext);
        }
    }
}
