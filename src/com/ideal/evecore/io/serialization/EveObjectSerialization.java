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
import com.ideal.evecore.io.StreamHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
            if (eveObject instanceof EveNoneObject) {
                jsonGenerator.writeString(EveNoneObject.NONE_LABEL);
            } else if (eveObject instanceof EveStringObject) {
                jsonGenerator.writeString(((EveStringObject) eveObject).getValue());
            } else if (eveObject instanceof EveNumberObject) {
                Number value = ((EveNumberObject) eveObject).getValue();

                if (value.doubleValue() == value.intValue()) {
                    jsonGenerator.writeNumber(value.intValue());
                } else {
                    jsonGenerator.writeNumber(value.doubleValue());
                }
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
                RemoteEveStructuredObject remote = (RemoteEveStructuredObject) eveObject;
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("type", EveStructuredObjectCategory.REMOTE.name());
                jsonGenerator.writeStringField(EveObject.DOMAIN_KEY, remote.getDomainId());
                jsonGenerator.writeStringField(EveObject.ID_KEY, remote.getObjectId());
                jsonGenerator.writeEndObject();
            } else if (eveObject instanceof EveQueryObject) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("type", EveStructuredObjectCategory.REMOTE.name());
                jsonGenerator.writeStringField(EveObject.DOMAIN_KEY, domainId);
                jsonGenerator.writeStringField(EveObject.ID_KEY, ((EveQueryObject) eveObject).getId());
                jsonGenerator.writeEndObject();
            } else if (eveObject instanceof EveMappingObject) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("type", EveStructuredObjectCategory.MAPPING.name());
                jsonGenerator.writeFieldName("value");
                jsonGenerator.writeStartObject();
                Mapping<EveObject> values = ((EveMappingObject) eveObject).getValues();
                for (Map.Entry<String, EveObject> entry : values.entrySet()) {
                    jsonGenerator.writeFieldName(entry.getKey());
                    serialize(entry.getValue(), jsonGenerator, serializerProvider);
                }
                jsonGenerator.writeEndObject();
                jsonGenerator.writeEndObject();
            } else if (eveObject instanceof EveDateObject) {
                Date d = ((EveDateObject) eveObject).getValue();
                jsonGenerator.writeString(EveDateObject.PLACE_HOLDER.format(d));
            } else {
                // TODO: other types
            }
        }
    }

    public static class EveObjectDeserializer extends JsonDeserializer<EveObject> {
        private final StreamHandler handler;

        public EveObjectDeserializer(StreamHandler h) {
            handler = h;
        }

        @Override
        public EveObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            switch (jsonParser.getCurrentToken()) {
                case VALUE_STRING:
                    String value = jsonParser.getText();
                    if (EveNoneObject.NONE_LABEL.equals(value)) {
                        return EveNoneObject.NONE;
                    } else if (EveDateObject.PLACE_HOLDER.matches(value)) {
                        return new EveDateObject(EveDateObject.PLACE_HOLDER.getValue(value));
                    } else {
                        return new EveStringObject(jsonParser.getText());
                    }
                case VALUE_TRUE:
                case VALUE_FALSE:
                    return new EveBooleanObject(jsonParser.getBooleanValue());
                case VALUE_NUMBER_FLOAT:
                    return new EveNumberObject(jsonParser.getDoubleValue());
                case VALUE_NUMBER_INT:
                    return new EveNumberObject(jsonParser.getIntValue());
                case START_OBJECT:
                    Mapping<EveObject> values = new Mapping<EveObject>();
                    EveStructuredObjectCategory category = EveStructuredObjectCategory.MAPPING;
                    String domainId = "";
                    String objectId = "";

                    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                        String field = jsonParser.getCurrentName();

                        if ("type".equals(field)) {
                            try {
                                category = EveStructuredObjectCategory.valueOf(jsonParser.getText());
                            } catch (Exception e) {

                            }
                        } else if ("value".equals(field)) {
                            jsonParser.nextToken(); // to dismiss JsonToken.START_OBJECT
                            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                                String attribute = jsonParser.getCurrentName();
                                values.put(attribute, deserialize(jsonParser, deserializationContext));
                            }
                        } else if (EveObject.DOMAIN_KEY.equals(field)) {
                            domainId = jsonParser.getText();
                        } else if (EveObject.ID_KEY.equals(field)) {
                            objectId = jsonParser.getText();
                        } else {
                            //jsonParser.nextToken();
                        }
                    }

                    switch (category) {
                        case MAPPING:
                            return new EveMappingObject(values);
                        case REMOTE:
                            return new RemoteEveStructuredObject(domainId, objectId, handler);
                    }

                    return new EveMappingObject(values);
                case START_ARRAY:
                    List<EveObject> elements = new ArrayList<EveObject>();
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        elements.add(deserialize(jsonParser, deserializationContext));
                    }
                    return new EveObjectList(elements);
                // TODO: EveDateObject, ...
                case VALUE_NULL:
                default:
                    return null;
            }
        }
    }
}
