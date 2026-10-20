package org.library.model;

public class Reader {

    private final long id;
    private final String ticketNumber;
    private final String fullName;
    private final String phone;
    private final String email;

    public Reader(long id, String ticketNumber, String fullName, String phone, String email) {
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Reader{id=" + id
                + ", ticketNumber='" + ticketNumber + '\''
                + ", fullName='" + fullName + '\''
                + ", phone='" + phone + '\''
                + ", email=" + email
                + '}';
    }
}
