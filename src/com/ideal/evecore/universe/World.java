package com.ideal.evecore.universe;

import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.universe.receiver.Receiver;
import com.ideal.evecore.util.Option;

/**
 * Created by Christophe on 22/04/2017.
 */
public interface World {
    /**
     * Adds a register to this world
     *
     * @param r The receiver to add
     */
    void registerReceiver(Receiver r);

    /**
     * Removes a receiver from this world
     *
     * @param r The receiver to remove
     */
    void unregisterReceiver(Receiver r);

    /**
     * Finds a receiver by checking if its mappings matches this EveStructuredObject
     *
     * @param o The object to match the mappings against
     * @return The receiver that cand handle this EveStructuredObject if any
     */
    Option<Receiver> findReceiver(EveStructuredObject o);
}
