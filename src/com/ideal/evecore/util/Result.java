package com.ideal.evecore.util;

/**
 * Created by chris on 06/04/2017.
 */
public abstract class Result<T> {
    abstract public boolean isSuccess();
    abstract public T get();
    abstract public Throwable getError();

    public static <T> Result<T> ok(T t){
        return new Success<T>(t);
    }

    public static <T> Result<T> ko(Throwable t){
        return new Failure<T>(t);
    }

    public static final class Success<T> extends Result<T> {
        private T value;

        public Success(T t){
            value = t;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Throwable getError() {
            return null;
        }
    }

    public static final class Failure<T> extends Result<T> {
        private Throwable error;

        public Failure(Throwable t){
            error = t;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public Throwable getError() {
            return error;
        }
    }
}
