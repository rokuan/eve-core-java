package com.ideal.evecore.io.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.interpreter.data.*;
import com.ideal.evecore.interpreter.remote.RemoteEveStructuredObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Christophe on 08/04/2017.
 */
public class EveObjectSerialization {
    public static class EveObjectSerializer extends JsonSerializer<EveObject> {
        private final String domainId;

        public EveObjectSerializer(String id) {
            domainId = id;
        }

        @Override
        public void serialize(EveObject eveObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            if (eveObject instanceof EveStringObject) {
                jsonGenerator.writeString(((EveStringObject)eveObject).getValue());
            } else if (eveObject instanceof EveNumberObject) {
                jsonGenerator.writeNumber(((EveNumberObject)eveObject).getValue().doubleValue());
            } else if (eveObject instanceof EveObjectList) {
                EveObjectList objects = (EveObjectList) eveObject;
                jsonGenerator.writeStartArray(objects.getValues().size());
                for (EveObject o : objects.getValues()) {
                    serialize(o, jsonGenerator, serializerProvider);
                }
                jsonGenerator.writeEndArray();
            } else if (eveObject instanceof EveBooleanObject) {
                jsonGenerator.writeBoolean(((EveBooleanObject) eveObject).getValue());
            } else if (eveObject instanceof RemoteEveStructuredObject) {

            } else if (eveObject instanceof EveQueryObject) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(EveObject.DOMAIN_KEY, domainId);
                jsonGenerator.writeStringField(EveObject.ID_KEY, ((EveQueryObject)eveObject).getId());
                jsonGenerator.writeEndObject();
            } else if (eveObject instanceof EveMappingObject) {
                jsonGenerator.writeStartObject();
                Mapping<EveObject> values = ((EveMappingObject)eveObject).getValues();
                for(Map.Entry<String, EveObject> entry: values.entrySet()){
                    jsonGenerator.writeFieldName(entry.getKey());
                    serialize(entry.getValue(), jsonGenerator, serializerProvider);
                }
                jsonGenerator.writeEndObject();
            }
        }
    }

    public static class EveObjectDeserializer extends JsonDeserializer<EveObject> {
        @Override
        public EveObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            switch(jsonParser.getCurrentToken()){
                case VALUE_STRING:
                    return new EveStringObject(jsonParser.getText());
                case VALUE_TRUE:
                case VALUE_FALSE:
                    return new EveBooleanObject(jsonParser.getBooleanValue());
                case VALUE_NUMBER_FLOAT:
                    return new EveNumberObject(jsonParser.getDoubleValue());
                case VALUE_NUMBER_INT:
                    return new EveNumberObject(jsonParser.getIntValue());
                case START_OBJECT:
                    Mapping<EveObject> values = new Mapping<EveObject>();
                    while(jsonParser.nextToken() != JsonToken.END_OBJECT) {
                        String field = jsonParser.nextFieldName();
                        values.put(field, deserialize(jsonParser, deserializationContext));
                    }
                    return new EveMappingObject(values);
                case START_ARRAY:
                    List<EveObject> elements = new ArrayList<EveObject>();
                    while(jsonParser.nextToken() != JsonToken.END_ARRAY){
                        elements.add(deserialize(jsonParser, deserializationContext));
                    }
                    return new EveObjectList(elements);
                case VALUE_NULL:
                default:
                    return null;
            }
        }
    }
}
