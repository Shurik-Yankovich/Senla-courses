package electronicbookstore.store;

import electronicbookstore.storage.Book;
import electronicbookstore.storage.Bookshelf;
import electronicbookstore.store.arrays.Order;
import electronicbookstore.store.arrays.Request;

import java.util.Calendar;
import java.util.List;

public interface Store {

    void addBookOnStorage(Book book);
    void addOrder(Customer customer, Book... books);
    void addOrder(Order bookOrder);
    int addRequest(Book book);
    void cancelOrder(Order bookOrder);
    boolean completeOrder(Order bookOrder);
    double earnedMoney(Calendar dateFrom, Calendar dateTo);
    List<Bookshelf> getBookList();
    List<Order> getCompletedOrderList(Calendar dateFrom, Calendar dateTo);
    int getCountCompletedOrder(Calendar dateFrom, Calendar dateTo);
    List<Order> getOrderList();
    List<Request> getRequestList();
    List<Bookshelf> getUnsoldBookList();

}
