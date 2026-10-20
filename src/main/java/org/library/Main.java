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

        System.out.println("=== Сценарий работы библиотеки ===\n");

        Reader ivanov = new Reader(1, "T-001", "Иван Иванов", "+375-900-111-22-33", "ivanov@mail.ru");
        Reader petrova = new Reader(2, "T-002", "Мария Петрова", "+375-900-444-55-66", null);
        library.addReader(ivanov);
        library.addReader(petrova);
        System.out.println("1. Зарегистрированы читатели:");
        System.out.println("   " + ivanov);
        System.out.println("   " + petrova);

        Book book1 = new Book(1, "Война и мир", "Лев Толстой", "978-5-00000-001-1", 1869);
        Book book2 = new Book(2, "Преступление и наказание", "Фёдор Достоевский", "978-5-00000-002-8", 1866);
        Book book3 = new Book(3, "Мастер и Маргарита", "Михаил Булгаков", "978-5-00000-003-5", 1967);
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        System.out.println("2. В фонд добавлены книги: " + library.getAllBooks().size() + " шт.");
        library.getAllBooks().forEach(b -> System.out.println("   " + b));

        System.out.println();

        // 3. Выдача книги первому читателю — успех
        long bookId = 1;
        long firstReaderId = 1;
        library.borrowBook(bookId, firstReaderId);
        Book borrowed = library.getBookById(bookId);
        System.out.println("3. Книга выдана первому читателю (успех):");
        System.out.println("   " + borrowed);

        // 4. Попытка выдать занятую книгу другому читателю — провал
        try {
            library.borrowBook(bookId, 2);
            System.out.println("4. НЕ ОЖИДАЛОСЬ: книга выдана второму читателю");
        } catch (Exception e) {
            System.out.println("4. Провал (занятую книгу нельзя выдать другому читателю):");
            System.out.println("   " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        // 5. Попытка выдать ту же книгу снова первому читателю — провал
        try {
            library.borrowBook(bookId, firstReaderId);
            System.out.println("5. НЕ ОЖИДАЛОСЬ: книга повторно выдана первому читателю");
        } catch (Exception e) {
            System.out.println("5. Провал (книга уже на руках у первого читателя):");
            System.out.println("   " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        // 6. Возврат книги первым читателем
        library.returnBook(bookId);
        System.out.println("6. Книга возвращена в фонд:");
        System.out.println("   " + library.getBookById(bookId));

        // 7. Попытка выдать возвращённую книгу несуществующему читателю — провал
        try {
            library.borrowBook(bookId, 999);
            System.out.println("7. НЕ ОЖИДАЛОСЬ: книга выдана несуществующему читателю");
        } catch (Exception e) {
            System.out.println("7. Провал (несуществующий читатель):");
            System.out.println("   " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        // 8. Попытка выдать возвращённую книгу второму читателю — успех
        library.borrowBook(bookId, 2);
        System.out.println("8. Свободная книга выдана второму читателю (успех):");
        System.out.println("   " + library.getBookById(bookId));

        System.out.println();

        // 9. Просмотр фонда и выданных книг
        System.out.println("9. Фонд (все книги):");
        library.getAllBooks().forEach(b -> System.out.println("   " + b));
        System.out.println("   Выданные книги:");
        library.getBorrowedBooks().forEach(b -> System.out.println("   " + b));

        System.out.println();

        // 10. Проверка валидности ISBN у книг (Apache Commons Validator)
        System.out.println("10. Проверка ISBN книг:");
        library.getAllBooks().forEach(b ->
                System.out.println("   \"" + b.getTitle() + "\" (" + b.getIsbn() + "): "
                        + (library.isIsbnValid(b) ? "валиден" : "НЕВАЛИДЕН")));

        // 11. Проверка валидности email у читателей (Apache Commons Validator)
        System.out.println("11. Проверка email читателей:");
        System.out.println("   " + ivanov.getFullName() + ": "
                + (library.isEmailValid(ivanov) ? "корректен" : "некорректен"));
        System.out.println("   " + petrova.getFullName() + ": "
                + (library.isEmailValid(petrova) ? "корректен" : "некорректен"));
    }
}
