package com.ideal.evecore.universe.matcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.io.serialization.ValueMatcherSerialization;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by chris on 10/04/2017.
 */
public class ValueMatcherUtils {
    private static final ObjectMapper VALUE_MATCHER_MAPPER;

    static {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule valueMatcherModule = new SimpleModule();
        valueMatcherModule.addDeserializer(ValueMatcher.class, new ValueMatcherSerialization.ValueMatcherDeserializer());
        mapper.registerModule(valueMatcherModule);
        VALUE_MATCHER_MAPPER = mapper;
    }

    public static Mapping<ValueMatcher> parseJson(File f) throws IOException {
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
            return parseJson(is);
        } finally {
            try { is.close(); } catch (Exception e) {}
        }
    }

    public static Mapping<ValueMatcher> parseJson(InputStream is) throws IOException {
        Mapping<ValueMatcher> mappings = new Mapping<ValueMatcher>();
        JsonNode o = VALUE_MATCHER_MAPPER.readTree(is);
        Iterator<Map.Entry<String, JsonNode>> entries = o.fields();

        while (entries.hasNext()) {
            Map.Entry<String, JsonNode> nextEntry = entries.next();
            mappings.put(nextEntry.getKey(), VALUE_MATCHER_MAPPER.treeToValue(nextEntry.getValue(), ValueMatcher.class));
        }

        return mappings;
    }
}
