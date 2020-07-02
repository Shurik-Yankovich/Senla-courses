package bookstore.model;

import bookstore.model.book.Book;

import static bookstore.model.Status.NEW;

public class Request {

    private Book book;
    private int count;
    private int index;
    private Status status;

    public Request(Book book) {
        this.book = book;
        this.count = 1;
        this.status = NEW;
    }

    public Book getBook() {
        return book;
    }

    public int getCount() {
        return count;
    }

    public int getIndex() {
        return index;
    }

    public Status getStatus() {
        return status;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request:" + book +
                ", status - " + status + '\n';
    }
}
