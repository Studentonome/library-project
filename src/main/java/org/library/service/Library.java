package org.library.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.ISBNValidator;
import org.library.model.Book;
import org.library.model.Reader;
import org.library.repository.BookRepository;
import org.library.repository.ReaderRepository;
import org.library.exception.BookAlreadyBorrowedException;
import org.library.exception.BookNotFoundException;
import org.library.exception.BookNotBorrowedException;
import org.library.exception.ReaderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class Library {

    private static final Logger LOG = LoggerFactory.getLogger(Library.class);

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
        LOG.info("Borrow request: bookId={}, readerId={}", bookId, readerId);

        try {
            Book book = bookRepository.findById(bookId);
            readerRepository.findById(readerId);

            if (book.isBorrowed()) {
                LOG.warn("Book is unavailable: bookId={} is already borrowed by readerId={}",
                        bookId, book.getReaderId());
                throw new BookAlreadyBorrowedException(bookId);
            }

            book.borrowTo(readerId);
            bookRepository.update(book);
            LOG.info("Book borrowed: bookId={}, readerId={}", bookId, readerId);
        } catch (BookNotFoundException | ReaderNotFoundException e) {
            LOG.error("Borrowing failed: bookId={}, readerId={}", bookId, readerId, e);
            throw e;
        }
    }

    public void returnBook(long bookId) {
        LOG.info("Return request: bookId={}", bookId);

        try {
            Book book = bookRepository.findById(bookId);

            if (!book.isBorrowed()) {
                LOG.warn("Return rejected: bookId={} is not borrowed", bookId);
                throw new BookNotBorrowedException(bookId);
            }

            book.returnToFund();
            bookRepository.update(book);
            LOG.info("Book returned to the collection: bookId={}", bookId);
        } catch (BookNotFoundException e) {
            LOG.error("Returning failed: bookId={}", bookId, e);
            throw e;
        }
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

    /** @return books available for lending (not held by any reader) */
    public List<Book> getAvailableBooks() {
        return bookRepository.getAll().stream()
                .filter(book -> !book.isBorrowed())
                .toList();
    }

    /** @return true if the book ISBN is valid (per ISBN-10/ISBN-13) */
    public boolean isIsbnValid(Book book) {
        return ISBN_VALIDATOR.isValid(book.getIsbn());
    }

    /** @return true if the reader email is valid; null or blank value is allowed (email not specified) */
    public boolean isEmailValid(Reader reader) {
        return reader.getEmail() == null
                || reader.getEmail().isBlank()
                || EMAIL_VALIDATOR.isValid(reader.getEmail());
    }
}
