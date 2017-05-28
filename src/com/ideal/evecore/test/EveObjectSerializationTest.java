package com.ideal.evecore.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.data.*;
import com.ideal.evecore.interpreter.remote.RemoteEveStructuredObject;
import com.ideal.evecore.io.serialization.EveObjectListSerialization;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.io.serialization.EveStructuredObjectSerialization;
import com.ideal.evecore.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Christophe on 09/04/2017.
 */
public class EveObjectSerializationTest {
    public ObjectMapper mapper;

    @Before
    public void initialize() {
        String domainId = "rcv1";
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(EveObject.class, new EveObjectSerialization.EveObjectSerializer(domainId));
        module.addSerializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectSerializer("com.eve.test"));
        module.addSerializer(EveObjectList.class, new EveObjectListSerialization.EveObjectListSerializer(domainId));
        module.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(null));
        module.addDeserializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectDeserializer(null));
        module.addDeserializer(EveObjectList.class, new EveObjectListSerialization.EveObjectListDeserializer(null));
        mapper.registerModule(module);
    }

    @Test
    public void testSerialization() throws IOException {
        EveObjectList list = new EveObjectList(
                new EveStringObject("Hello"),
                new EveNumberObject(7),
                new EveNumberObject(1.0),
                new EveBooleanObject(true),
                new EveBooleanObject(false)
        );
        String result = mapper.writeValueAsString(list);
        System.out.println(result);
        EveObjectList conversion = mapper.readValue(result, EveObjectList.class);
        for (EveObject o : conversion.getValues()) {
            System.out.println(o);
        }
    }

    @Test
    public void testObjectSerialization() throws IOException {
        //EveStructuredObject eso = new RemoteEveStructuredObject("com.eve.test", UUID.randomUUID().toString(), null);
        EveStructuredObject eso = new EveMappingObject(
                new Pair<String, EveObject>("bonjour", new EveStringObject("hello")),
                new Pair<String, EveObject>("n", new EveNumberObject(7)),
                new Pair<String, EveObject>("activated", new EveBooleanObject(false))
        );
        String result = mapper.writeValueAsString(eso);
        System.out.println(result);
        EveStructuredObject conversion = mapper.readValue(result, EveStructuredObject.class);
        System.out.println(conversion);
        if (conversion instanceof RemoteEveStructuredObject) {
            RemoteEveStructuredObject remote = (RemoteEveStructuredObject) conversion;
            System.out.println("[REMOTE] " + remote.getDomainId() + " / " + remote.getObjectId());
        } else if (conversion instanceof EveMappingObject) {
            EveMappingObject mapping = (EveMappingObject) conversion;
            for (Map.Entry<String, EveObject> entry : mapping.getValues().entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    @Test
    public void testDateSerialization() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        EveDateObject o = new EveDateObject(new Date());
        String result = mapper.writeValueAsString(o);
        System.out.println(result);
        EveDateObject d = (EveDateObject) mapper.readValue(result, EveObject.class);
        System.out.println(d.getValue());
    }
}