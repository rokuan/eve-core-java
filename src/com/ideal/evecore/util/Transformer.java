package com.ideal.evecore.util;

/**
 * Created by chris on 06/04/2017.
 */
public interface Transformer<T, R> {
    public R apply(T t);
}
