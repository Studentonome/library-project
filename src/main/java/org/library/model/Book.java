package org.library.model;

public class Book {

    private final long id;
    private final String title;
    private final String author;
    private final String isbn;
    private final int year;

    /** Идентификатор читателя, у которого книга на руках; null, если книга свободна. */
    private Long readerId;

    public Book(long id, String title, String author, String isbn, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.readerId = null;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public Long getReaderId() {
        return readerId;
    }

    /** @return true, если книга находится на руках у читателя */
    public boolean isBorrowed() {
        return readerId != null;
    }

    public void borrowTo(long readerId) {
        this.readerId = readerId;
    }

    public void returnToFund() {
        this.readerId = null;
    }

    @Override
    public String toString() {
        return "Book{id=" + id
                + ", title='" + title + '\''
                + ", author='" + author + '\''
                + ", isbn='" + isbn + '\''
                + ", year=" + year
                + ", readerId=" + readerId
                + '}';
    }
}
