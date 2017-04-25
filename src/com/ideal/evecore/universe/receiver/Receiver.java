package com.ideal.evecore.universe.receiver;

import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.util.Result;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface Receiver {
    /**
     * Initializes the receiver
     */
    void initReceiver();

    /**
     * Destroys the receiver
     */
    void destroyReceiver();

    /**
     * Retrieves this receiver's name
     * @return The name of this receiver
     */
    String getReceiverName();

    /**
     * Gets the definition field for this receiver (i.e. the messages this receiver can handle)
     * @return
     */
    Mapping<ValueMatcher> getMappings();

    /**
     * Executes a message and gets the output if the execution is successful
     * @param message The message to process
     * @return
     */
    Result<EveObject> handleMessage(EveObjectMessage message);
}
