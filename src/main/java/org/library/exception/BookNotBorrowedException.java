package org.library.exception;

public class BookNotBorrowedException extends RuntimeException {

    public BookNotBorrowedException(long bookId) {
        super("Книга с id=" + bookId + " не находится на руках и не может быть возвращена");
    }
}
