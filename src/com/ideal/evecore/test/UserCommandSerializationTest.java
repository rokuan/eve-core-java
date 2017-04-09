package com.ideal.evecore.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStringObject;
import com.ideal.evecore.io.command.structured.SetFieldCommand;
import com.ideal.evecore.io.command.user.ObjectRequestCommand;
import com.ideal.evecore.io.command.user.UserCommand;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Christophe on 09/04/2017.
 */
public class UserCommandSerializationTest {
    private ObjectMapper mapper;

    @Before
    public void initialize(){
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(null));
        mapper.registerModule(module);
    }

    @Test
    public void serializeCommand() throws IOException {
        SetFieldCommand command = new SetFieldCommand("name", new EveStringObject("Cuisse"));
        ObjectRequestCommand objectCommand = new ObjectRequestCommand("rcv1", "my_location", command);
        String json = mapper.writeValueAsString(objectCommand);
        System.out.println(json);
        UserCommand conversion = mapper.readValue(json, UserCommand.class);
        System.out.println(((ObjectRequestCommand)conversion).getObjectCommand());
    }
}
