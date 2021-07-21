package me.earth.phobos.util;

public
class NoStackTraceThrowable extends RuntimeException {

    public
    NoStackTraceThrowable ( final String msg ) {
        super ( msg );
        this.setStackTrace ( new StackTraceElement[0] );
    }

    @Override
    public synchronized
    Throwable fillInStackTrace ( ) {
        return this;
    }
}
