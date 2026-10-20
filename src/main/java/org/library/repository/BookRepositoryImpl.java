package org.library.repository;

import org.library.model.Book;
import org.library.exception.BookNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private final List<Book> books = new ArrayList<>();

    @Override
    public void add(Book book) {
        books.add(book);
    }

    @Override
    public Book findById(long id) {
        return books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public List<Book> findByIsbn(String isbn) {
        return books.stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .toList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return books.stream()
                .filter(b -> b.getTitle().equals(title))
                .toList();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(b -> b.getAuthor().equals(author))
                .toList();
    }

    @Override
    public List<Book> getAll() {
        return List.copyOf(books);
    }

    @Override
    public void update(Book book) {
        int index = indexOf(book.getId());
        books.set(index, book);
    }

    @Override
    public void delete(long id) {
        books.removeIf(b -> b.getId() == id);
    }

    private int indexOf(long id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                return i;
            }
        }
        throw new BookNotFoundException(id);
    }
}
