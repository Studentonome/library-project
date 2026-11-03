package org.library.exception;

public class ReaderNotFoundException extends RuntimeException {

    public ReaderNotFoundException(long id) {
        super("Reader with id=" + id + " was not found");
    }
}
