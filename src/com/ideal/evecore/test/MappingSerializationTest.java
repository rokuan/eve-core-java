import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.io.serialization.ValueMatcherSerialization;
import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.universe.matcher.ValueMatcherUtils;
import org.junit.Before;
import org.junit.Test;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Christophe on 04/05/2017.
 */
public class MappingSerializationTest {
    private ObjectMapper mapper = new ObjectMapper();
    private MapLikeType mappingType;

    @Before
    public void initialize() {
        mappingType = mapper.getTypeFactory().constructMapLikeType(Mapping.class, String.class, ValueMatcher.class);
        SimpleModule valueMatcherModule = new SimpleModule();
        valueMatcherModule.addSerializer(ValueMatcher.class, new ValueMatcherSerialization.ValueMatcherSerializer());
        valueMatcherModule.addDeserializer(ValueMatcher.class, new ValueMatcherSerialization.ValueMatcherDeserializer());
        mapper.registerModule(valueMatcherModule);
    }

    @Test
    public void testSerialization() throws IOException {
        File f = new File("receiver.json");
        Mapping<ValueMatcher> mappings = ValueMatcherUtils.parseJson(f);
        System.out.println(mapper.writerFor(mappingType).writeValueAsString(mappings));
    }

    @Test
    public void testDeserialization() throws IOException {
        File f = new File("receiver.json");
        Mapping<ValueMatcher> mappings = ValueMatcherUtils.parseJson(f);
        String json = mapper.writerFor(mappingType).writeValueAsString(mappings);
        Mapping<ValueMatcher> conversion = mapper.readerFor(mappingType).readValue(json);
        System.out.println(conversion);
    }
}
