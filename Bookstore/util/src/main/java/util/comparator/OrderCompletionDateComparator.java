package util.comparator;

import entity.Order;

import java.util.Comparator;

public class OrderCompletionDateComparator implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2) {
        return order1.getOrderCompletionDate().compareTo(order2.getOrderCompletionDate());
    }
}