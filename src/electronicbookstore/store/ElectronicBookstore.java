package electronicbookstore.store;

import electronicbookstore.storage.Book;
import electronicbookstore.storage.Bookshelf;
import electronicbookstore.storage.Storage;
import electronicbookstore.store.arrays.BookOrder;
import electronicbookstore.store.arrays.BookRequest;
import electronicbookstore.store.arrays.OrderArray;
import electronicbookstore.store.arrays.RequestArray;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static electronicbookstore.store.Status.CANCELED;
import static electronicbookstore.store.Status.COMPLETED;

public class ElectronicBookstore implements Store {

    private static final String BOOK_NOT_FOUND = "Данной книги нет в списке!";

    Storage bookStorage;
    OrderArray orderList;
    RequestArray requestList;

    public ElectronicBookstore(Storage bookStorage) {
        this.bookStorage = bookStorage;
        orderList = new OrderArray();
        requestList = new RequestArray();
    }

    @Override
    public void addBookOnStorage(Book book) {
        requestList.closeRequest(book);
        bookStorage.comingBook(book);
    }

    @Override
    public void addOrder(Customer customer, Book... books) {
        BookOrder bookOrder = new BookOrder(customer, new GregorianCalendar(), books);
        double price = getTotalPrice(books);
        bookOrder.setPrice(price);
        int[] numbersRequest = checkBooksOnStorage(books);
        bookOrder.setNumbersRequest(numbersRequest);
        orderList.add(bookOrder);
    }

    private double getTotalPrice(Book[] books) {
        double price = 0;
        for (Book book : books) {
            price += bookStorage.getBookshelf(book).getPrice();
        }
        return price;
    }

    private int[] checkBooksOnStorage(Book[] books) {
        int[] result = new int[0];
        int index;
        Bookshelf bookshelf;

        for (Book book : books) {
            bookshelf = bookStorage.getBookshelf(book);
            if (bookshelf == null) {
                System.out.println(BOOK_NOT_FOUND);
            } else if (!bookshelf.isPresence()) {
                index = result.length;
                result = Arrays.copyOf(result, index + 1);
                result[index] = addRequest(book);
            }
        }

        return result;
    }

    @Override
    public int addRequest(Book book) {
        return requestList.add(new BookRequest(book));
    }

    @Override
    public void cancelOrder(BookOrder bookOrder) {
        for (int number : bookOrder.getNumbersRequest()) {
            requestList.changeStatus(number, CANCELED);
        }
        orderList.changeOrderStatus(bookOrder, CANCELED);
    }

    @Override
    public boolean completeOrder(BookOrder bookOrder) {
        boolean result = true;

        for (int number : bookOrder.getNumbersRequest()) {
            if (requestList.getByRequestNumber(number).getStatus() != COMPLETED) {
                result = false;
            }
        }
        if (result) {
            orderList.changeOrderStatus(bookOrder, COMPLETED);
        }

        return result;
    }

    @Override
    public double earnedMoney(Calendar dateFrom, Calendar dateTo) {
        BookOrder[] bookOrders = getCompletedOrderList(dateFrom, dateTo);
        double money = 0;
        for (BookOrder order : bookOrders) {
            money += getTotalPrice(order.getBooks());
        }
        return money;
    }

    @Override
    public Bookshelf[] getBookList() {
        return bookStorage.getBookshelfList();
    }

    @Override
    public BookOrder[] getCompletedOrderList(Calendar dateFrom, Calendar dateTo) {
        return orderList.getCompletedOrder(dateFrom, dateTo);
    }

    @Override
    public int getCountCompletedOrder(Calendar dateFrom, Calendar dateTo) {
        return getCompletedOrderList(dateFrom, dateTo).length;
    }

    @Override
    public BookOrder[] getOrderList() {
        return orderList.getSortingArray();
    }

    @Override
    public BookRequest[] getRequestList() {
        return requestList.getArray();
    }

    @Override
    public Bookshelf[] getUnsoldBookList() {
        return bookStorage.getUnsoldBookshelfList();
    }
}
