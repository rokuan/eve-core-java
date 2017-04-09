package com.ideal.evecore.universe.receiver;

import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.util.Result;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface Receiver {
    void initReceiver();
    void destroyReceiver();
    String getReceiverName();
    Mapping<ValueMatcher> getMappings();
    Result<EveObject> handleMessage(EveObjectMessage message);
}
