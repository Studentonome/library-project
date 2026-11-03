package org.library;

import org.library.model.Book;
import org.library.model.Reader;
import org.library.repository.BookRepository;
import org.library.repository.BookRepositoryImpl;
import org.library.repository.ReaderRepository;
import org.library.repository.ReaderRepositoryImpl;
import org.library.service.Library;

public class Main {

    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepositoryImpl();
        ReaderRepository readerRepository = new ReaderRepositoryImpl();
        Library library = new Library(bookRepository, readerRepository);

        System.out.println("=== Library scenario ===\n");

        Reader ivanov = new Reader(1, "T-001", "Ivan Ivanov", "+375-900-111-22-33", "ivanov@mail.ru");
        Reader petrova = new Reader(2, "T-002", "Maria Petrova", "+375-900-444-55-66", null);
        library.addReader(ivanov);
        library.addReader(petrova);
        System.out.println("1. Registered readers:");
        System.out.println("   " + ivanov);
        System.out.println("   " + petrova);

        Book book1 = new Book(1, "War and Peace", "Leo Tolstoy", "978-5-00000-001-1", 1869);
        Book book2 = new Book(2, "Crime and Punishment", "Fyodor Dostoevsky", "978-5-00000-002-8", 1866);
        Book book3 = new Book(3, "The Master and Margarita", "Mikhail Bulgakov", "978-5-00000-003-5", 1967);
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        System.out.println("2. Books added to the collection: " + library.getAllBooks().size());
        library.getAllBooks().forEach(b -> System.out.println("   " + b));

        System.out.println();

        long bookId = 1;
        long firstReaderId = 1;
        library.borrowBook(bookId, firstReaderId);
        Book borrowed = library.getBookById(bookId);
        System.out.println("3. Book borrowed by the first reader (success):");
        System.out.println("   " + borrowed);

        try {
            library.borrowBook(bookId, 2);
            System.out.println("4. UNEXPECTED: book borrowed by the second reader");
        } catch (Exception e) {
            System.out.println("4. Failure (a borrowed book cannot be lent to another reader):");
            System.out.println("   " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        try {
            library.borrowBook(bookId, firstReaderId);
            System.out.println("5. UNEXPECTED: book borrowed again by the first reader");
        } catch (Exception e) {
            System.out.println("5. Failure (the book is already held by the first reader):");
            System.out.println("   " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        library.returnBook(bookId);
        System.out.println("6. Book returned to the collection:");
        System.out.println("   " + library.getBookById(bookId));

        try {
            library.borrowBook(bookId, 999);
            System.out.println("7. UNEXPECTED: book borrowed by an unknown reader");
        } catch (Exception e) {
            System.out.println("7. Failure (unknown reader):");
            System.out.println("   " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        library.borrowBook(bookId, 2);
        System.out.println("8. Available book borrowed by the second reader (success):");
        System.out.println("   " + library.getBookById(bookId));

        System.out.println();

        System.out.println("9. Collection (all books):");
        library.getAllBooks().forEach(b -> System.out.println("   " + b));
        System.out.println("   Borrowed books:");
        library.getBorrowedBooks().forEach(b -> System.out.println("   " + b));

        System.out.println();

        System.out.println("10. ISBN validation:");
        library.getAllBooks().forEach(b ->
                System.out.println("   \"" + b.getTitle() + "\" (" + b.getIsbn() + "): "
                        + (library.isIsbnValid(b) ? "valid" : "INVALID")));

        System.out.println("11. Reader email validation:");
        System.out.println("   " + ivanov.getFullName() + ": "
                + (library.isEmailValid(ivanov) ? "valid" : "invalid"));
        System.out.println("   " + petrova.getFullName() + ": "
                + (library.isEmailValid(petrova) ? "valid" : "invalid"));
    }
}
