package org.library.exception;

public class BookNotBorrowedException extends RuntimeException {

    public BookNotBorrowedException(long bookId) {
        super("Book with id=" + bookId + " is not borrowed and cannot be returned");
    }
}
