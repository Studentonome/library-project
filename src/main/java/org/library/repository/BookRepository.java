package org.library.repository;

import org.library.model.Book;
import java.util.List;

public interface BookRepository {

    void add(Book book);

    Book findById(long id);

    List<Book> findByIsbn(String isbn);

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> getAll();

    void update(Book book);

    void delete(long id);
}
