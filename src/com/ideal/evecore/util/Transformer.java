package com.ideal.evecore.util;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface Transformer<T, R> {
    public R apply(T t);
}
