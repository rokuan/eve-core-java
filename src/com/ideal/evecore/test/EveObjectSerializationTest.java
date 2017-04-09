package com.ideal.evecore.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.data.*;
import com.ideal.evecore.io.serialization.EveObjectListSerialization;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

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
        module.addSerializer(EveObjectList.class, new EveObjectListSerialization.EveObjectListSerializer(domainId));
        module.addSerializer(EveObject.class, new EveObjectSerialization.EveObjectSerializer(domainId));
        module.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(null));
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
        for (EveObject o: conversion.getValues()) {
            System.out.println(o);
        }
    }
}