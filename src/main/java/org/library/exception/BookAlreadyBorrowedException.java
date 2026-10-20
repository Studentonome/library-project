package org.library.exception;

public class BookAlreadyBorrowedException extends RuntimeException {

    public BookAlreadyBorrowedException(long bookId) {
        super("Книга с id=" + bookId + " уже находится на руках и не может быть выдана");
    }
}
