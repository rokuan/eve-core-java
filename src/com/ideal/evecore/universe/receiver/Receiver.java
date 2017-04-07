package com.ideal.evecore.universe.receiver;

import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.util.Result;

/**
 * Created by chris on 06/04/2017.
 */
public interface Receiver {
    void initReceiver();
    void destroyReceiver();
    Result<EveObject> handleMessage(EveObjectMessage message);
}
