package org.library.repository;

import org.library.model.Reader;
import org.library.exception.ReaderNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReaderRepositoryImpl implements ReaderRepository {

    private final List<Reader> readers = new ArrayList<>();

    @Override
    public void add(Reader reader) {
        readers.add(reader);
    }

    @Override
    public Reader findById(long id) {
        return readers.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ReaderNotFoundException(id));
    }

    @Override
    public List<Reader> findByTicketNumber(String ticketNumber) {
        return readers.stream()
                .filter(r -> r.getTicketNumber().equals(ticketNumber))
                .toList();
    }

    @Override
    public List<Reader> findByFullName(String fullName) {
        return readers.stream()
                .filter(r -> r.getFullName().equals(fullName))
                .toList();
    }

    @Override
    public List<Reader> getAll() {
        return List.copyOf(readers);
    }

    @Override
    public void delete(long id) {
        readers.removeIf(r -> r.getId() == id);
    }
}
