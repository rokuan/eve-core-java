package com.ideal.evecore.universe;

import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.universe.receiver.Receiver;
import com.ideal.evecore.util.Option;

/**
 * Created by Christophe on 22/04/2017.
 */
public interface World {
    void registerReceiver(Receiver r);
    void unregisterReceiver(Receiver r);
    Option<Receiver> findReceiver(EveStructuredObject o);
}
