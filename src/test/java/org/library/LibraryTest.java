package org.library;

import org.library.model.Book;
import org.library.model.Reader;
import org.library.repository.BookRepository;
import org.library.repository.BookRepositoryImpl;
import org.library.repository.ReaderRepository;
import org.library.repository.ReaderRepositoryImpl;
import org.library.service.Library;
import org.library.exception.BookAlreadyBorrowedException;
import org.library.exception.BookNotBorrowedException;
import org.library.exception.ReaderNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class LibraryTest {

    private Library library;
    private Reader reader1;
    private Reader reader2;
    private Book book1;

    @BeforeEach
    void setUp() {
        BookRepository bookRepository = new BookRepositoryImpl();
        ReaderRepository readerRepository = new ReaderRepositoryImpl();
        library = new Library(bookRepository, readerRepository);

        reader1 = new Reader(1, "T-001", "Ivan Ivanov", "+375-900-22-33", "ivanov@mail.ru");
        reader2 = new Reader(2, "T-002", "Maria Petrova", "+375-900-55-66", null);
        library.addReader(reader1);
        library.addReader(reader2);

        book1 = new Book(1, "War and Peace", "Leo Tolstoy", "978-5-00000-001-1", 1869);
        library.addBook(book1);
        library.addBook(new Book(2, "Crime and Punishment", "Fyodor Dostoevsky", "978-5-00000-002-8", 1866));
        library.addBook(new Book(3, "The Master and Margarita", "Mikhail Bulgakov", "978-5-00000-003-5", 1967));
    }

    @Test
    void availableBookIsBorrowedSuccessfully() {
        library.borrowBook(book1.getId(), reader1.getId());

        Book borrowed = library.getBookById(book1.getId());
        assertTrue(borrowed.isBorrowed(), "book must be borrowed");
        assertEquals(reader1.getId(), borrowed.getReaderId(), "readerId must be set on the book");

        List<Book> borrowedBooks = library.getBorrowedBooks();
        assertTrue(borrowedBooks.stream().anyMatch(b -> b.getId() == book1.getId()),
                "book must appear in getBorrowedBooks()");
    }

    @Test
    void alreadyBorrowedBookCannotBeBorrowedBySameReader() {
        library.borrowBook(book1.getId(), reader1.getId());

        assertThrows(BookAlreadyBorrowedException.class,
                () -> library.borrowBook(book1.getId(), reader1.getId()),
                "borrowing the same book twice must throw an exception");
    }

    @Test
    void borrowedBookCannotBeGivenToAnotherReader() {
        library.borrowBook(book1.getId(), reader1.getId());

        assertThrows(BookAlreadyBorrowedException.class,
                () -> library.borrowBook(book1.getId(), reader2.getId()),
                "a borrowed book must not be lent to another reader");
    }

    @Test
    void borrowedBookIsReturnedSuccessfully() {
        library.borrowBook(book1.getId(), reader1.getId());

        library.returnBook(book1.getId());

        Book returned = library.getBookById(book1.getId());
        assertFalse(returned.isBorrowed(), "after return the book must be free");
        assertNull(returned.getReaderId(), "a returned book must not keep readerId");
        assertTrue(library.getBorrowedBooks().isEmpty(), "the returned book must not be listed as borrowed");
    }

    @Test
    void returningFreeBookThrowsException() {
        assertThrows(BookNotBorrowedException.class,
                () -> library.returnBook(book1.getId()),
                "returning a free book must throw an exception");
    }

    // error scenario: borrowing for an unknown reader is logged at ERROR level
    @Test
    void borrowingForUnknownReaderThrowsException() {
        assertThrows(ReaderNotFoundException.class,
                () -> library.borrowBook(book1.getId(), 999),
                "borrowing for an unknown reader must throw an exception");
        assertFalse(library.getBookById(book1.getId()).isBorrowed(), "the book must remain free");
    }

    @Test
    void availableBooksAreReturned() {
        library.borrowBook(book1.getId(), reader1.getId());

        List<Book> available = library.getAvailableBooks();
        assertEquals(2, available.size(), "only free books must be listed");
        assertTrue(available.stream().noneMatch(Book::isBorrowed), "no borrowed book must be listed");
        assertTrue(available.stream().noneMatch(b -> b.getId() == book1.getId()),
                "the borrowed book must not appear among available books");
    }
}
