package org.library.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.ISBNValidator;
import org.library.model.Book;
import org.library.model.Reader;
import org.library.repository.BookRepository;
import org.library.repository.ReaderRepository;
import org.library.exception.BookAlreadyBorrowedException;
import org.library.exception.BookNotBorrowedException;
import java.util.List;

public class Library {

    private static final ISBNValidator ISBN_VALIDATOR = ISBNValidator.getInstance();
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    public Library(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    public void addReader(Reader reader) {
        readerRepository.add(reader);
    }

    public void addBook(Book book) {
        bookRepository.add(book);
    }

    public void borrowBook(long bookId, long readerId) {
        Book book = bookRepository.findById(bookId);
        readerRepository.findById(readerId);

        if (book.isBorrowed()) {
            throw new BookAlreadyBorrowedException(bookId);
        }

        book.borrowTo(readerId);
        bookRepository.update(book);
    }

    public void returnBook(long bookId) {
        Book book = bookRepository.findById(bookId);

        if (!book.isBorrowed()) {
            throw new BookNotBorrowedException(bookId);
        }

        book.returnToFund();
        bookRepository.update(book);
    }

    public Book getBookById(long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> getBookByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public Reader getReaderById(long id) {
        return readerRepository.findById(id);
    }

    public List<Reader> getReaderByTicketNumber(String ticketNumber) {
        return readerRepository.findByTicketNumber(ticketNumber);
    }

    public List<Reader> getReaderByFullName(String fullName) {
        return readerRepository.findByFullName(fullName);
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    public List<Book> getBorrowedBooks() {
        return bookRepository.getAll().stream()
                .filter(Book::isBorrowed)
                .toList();
    }

    /** @return true, если ISBN книги корректен (по стандарту ISBN-10/ISBN-13) */
    public boolean isIsbnValid(Book book) {
        return ISBN_VALIDATOR.isValid(book.getIsbn());
    }

    /** @return true, если email читателя корректен; null/пустое значение допустимо (email не указан) */
    public boolean isEmailValid(Reader reader) {
        return reader.getEmail() == null
                || reader.getEmail().isBlank()
                || EMAIL_VALIDATOR.isValid(reader.getEmail());
    }
}
