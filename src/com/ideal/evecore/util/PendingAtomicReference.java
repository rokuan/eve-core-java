package com.ideal.evecore.util;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Christophe on 07/04/2017.
 */
public class PendingAtomicReference<T> extends PendingAtomicValue<T> {
    protected AtomicReference<T> value = new AtomicReference<T>();

    @Override
    public void set(T t) {
        value.set(t);
        modified.set(true);
        synchronized (value) {
            value.notify();
        }
    }

    @Override
    public T get() {
        while(!modified.get()){
            synchronized (value) {
                try {
                    value.wait();
                } catch (InterruptedException e) {

                }
            }
        }
        return value.get();
    }
}
