package com.ideal.evecore.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by chris on 07/04/2017.
 */
public abstract class PendingAtomicValue<T> {
    protected final AtomicBoolean modified = new AtomicBoolean(false);

    abstract public void set(T t);
    abstract public T get();
}
