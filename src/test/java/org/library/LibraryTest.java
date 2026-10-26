package org.library;

import org.library.model.Book;
import org.library.model.Reader;
import org.library.repository.BookRepository;
import org.library.repository.BookRepositoryImpl;
import org.library.repository.ReaderRepository;
import org.library.repository.ReaderRepositoryImpl;
import org.library.service.Library;
import org.library.exception.BookAlreadyBorrowedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        reader1 = new Reader(1, "T-001", "Иван Иванов", "+375-900-22-33", "ivanov@mail.ru");
        reader2 = new Reader(2, "T-002", "Мария Петрова", "+375-900-55-66", null);
        library.addReader(reader1);
        library.addReader(reader2);

        book1 = new Book(1, "Война и мир", "Лев Толстой", "978-5-00000-001-1", 1869);
        library.addBook(book1);
        library.addBook(new Book(2, "Преступление и наказание", "Фёдор Достоевский", "978-5-00000-002-8", 1866));
        library.addBook(new Book(3, "Мастер и Маргарита", "Михаил Булгаков", "978-5-00000-003-5", 1967));
    }

    // доступная книга успешно выдаётся читателю
    @Test
    void availableBookIsBorrowedSuccessfully() {
        library.borrowBook(book1.getId(), reader1.getId());

        Book borrowed = library.getBookById(book1.getId());
        assertTrue(borrowed.isBorrowed(), "книга должна быть занятой");
        assertEquals(reader1.getId(), borrowed.getReaderId(), "у книги должен быть проставлен readerId читателя");

        List<Book> borrowedBooks = library.getBorrowedBooks();
        assertTrue(borrowedBooks.stream().anyMatch(b -> b.getId() == book1.getId()),
                "книга должна появиться в списке getBorrowedBooks()");
    }

    // уже занятую книгу нельзя взять повторно (тем же читателем)
    @Test
    void alreadyBorrowedBookCannotBeBorrowedBySameReader() {
        library.borrowBook(book1.getId(), reader1.getId());

        assertThrows(BookAlreadyBorrowedException.class,
                () -> library.borrowBook(book1.getId(), reader1.getId()),
                "повторная выдача той же книги должна выбрасывать исключение");
    }

    // книга на руках у одного читателя не выдаётся другому
    @Test
    void borrowedBookCannotBeGivenToAnotherReader() {
        library.borrowBook(book1.getId(), reader1.getId());

        assertThrows(BookAlreadyBorrowedException.class,
                () -> library.borrowBook(book1.getId(), reader2.getId()),
                "книга с чужих рук не должна выдаваться другому читателю");
    }
}
