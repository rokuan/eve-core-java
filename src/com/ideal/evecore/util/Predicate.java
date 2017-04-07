package com.ideal.evecore.util;

/**
 * Created by chris on 06/04/2017.
 */
public interface Predicate<T> {
    boolean matches(T t);
}
