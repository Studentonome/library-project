package org.library.repository;

import org.library.model.Reader;
import java.util.List;

public interface ReaderRepository {

    void add(Reader reader);

    Reader findById(long id);

    List<Reader> findByTicketNumber(String ticketNumber);

    List<Reader> findByFullName(String fullName);

    List<Reader> getAll();

    void delete(long id);
}
