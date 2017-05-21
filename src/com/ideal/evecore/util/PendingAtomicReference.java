package com.ideal.evecore.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Christophe on 07/04/2017.
 */
public class PendingAtomicReference<T> {
    protected LinkedBlockingQueue<T> value = new LinkedBlockingQueue<T>(1);

    public void set(T t) {
        value.offer(t);
    }

    public T get() throws InterruptedException {
        return value.take();
    }
}
