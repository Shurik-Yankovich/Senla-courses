package electronicbookstore.storage;

public class Book implements Comparable<Book> {

    private String title;
    private String author;
    private int publicationYear;

    public Book(String title, String author, int publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public String toString() {
        return String.format("[Book: \"%s\" - %s, %d]", title, author, publicationYear);
    }

    public boolean equals(Book book) {
        return toString().equals(book.toString());
    }

    @Override
    public int compareTo(Book book) {
        return toString().compareTo(book.toString());
    }
}
