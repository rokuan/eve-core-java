import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.data.*;
import com.ideal.evecore.io.serialization.EveStructuredObjectSerialization;
import com.ideal.evecore.universe.receiver.EveObjectMessage;
import com.ideal.evecore.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


/**
 * Created by Christophe on 06/05/2017.
 */
public class ReceiverSerializationTest {
    private ObjectMapper mapper;
    private EveObjectMessage message;

    @Before
    public void initialize() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectSerializer("1"));
        module.addDeserializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectDeserializer(null));
        mapper.registerModule(module);

        EveStructuredObject o = new EveMappingObject(
                new Pair<String, EveObject>("eve_type", new EveStringObject("location")),
                new Pair<String, EveObject>("latitude", new EveNumberObject(42.7)),
                new Pair<String, EveObject>("longitude", new EveNumberObject(2.3))
        );
        message = new EveObjectMessage(o);
    }

    @Test
    public void testEveObjectMessageSerialization() throws IOException {
        String json = mapper.writeValueAsString(message);
        System.out.println(json);
        EveObjectMessage original = mapper.readValue(json, EveObjectMessage.class);
    }
}
