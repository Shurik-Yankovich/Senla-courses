package bookstore.util.csv;

import bookstore.exeption.StatusException;

import java.io.IOException;
import java.util.List;

public interface CsvUtil <T> {

    void writeToCsv(T t) throws IOException;
    void writeAllToCsv(List<T> t) throws IOException;
    T readFromCsv(int id) throws IOException, StatusException;
    List<T> readAllFromCsv() throws IOException, StatusException;
}
