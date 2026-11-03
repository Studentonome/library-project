package org.library.exception;

public class BookAlreadyBorrowedException extends RuntimeException {

    public BookAlreadyBorrowedException(long bookId) {
        super("Book with id=" + bookId + " is already borrowed and cannot be lent");
    }
}
