package org.library.exception;

public class ReaderNotFoundException extends RuntimeException {

    public ReaderNotFoundException(long id) {
        super("Читатель с id=" + id + " не найден");
    }
}
