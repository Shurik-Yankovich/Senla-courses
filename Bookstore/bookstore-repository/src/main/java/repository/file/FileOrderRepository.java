package repository.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import util.csv.OrderCsv;
import entity.Order;
import exeption.RepositoryException;
import repository.base.OrderRepository;

import java.io.IOException;
import java.util.List;

@Repository
public class FileOrderRepository implements OrderRepository {

    @Autowired
    private OrderCsv orderCsv;

    @Override
    public Order create(Order order) throws RepositoryException {
        try {
            List<Order> orderList = orderCsv.readAllFromCsv();
            for (Order bookOrder : orderList) {
                if (bookOrder.getId() == order.getId()) {
                    order.setId(orderList.size());
                }
            }
            orderCsv.writeToCsv(order);
            return order;
        } catch (IOException e) {
            throw new RepositoryException("Ошибка записи заказа в файл!");
        }
    }

    @Override
    public Order update(Order order) throws RepositoryException {
        try {
            List<Order> orderList = orderCsv.readAllFromCsv();
            boolean isPresent = false;
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getId() == order.getId()) {
                    orderList.set(i, order);
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) {
                orderList.add(order);
            }
            orderCsv.writeAllToCsv(orderList);
            return order;
        } catch (IOException e) {
            throw new RepositoryException("Ошибка обновления заказа в файле!");
        }
    }

    @Override
    public Order read(Integer id) throws RepositoryException {
        try {
            return orderCsv.readFromCsv(id);
        } catch (IOException e) {
            throw new RepositoryException("Ошибка чтения заказа из файла!");
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public List<Order> readAll() throws RepositoryException {
        try {
            return orderCsv.readAllFromCsv();
        } catch (IOException e) {
            throw new RepositoryException("Ошибка чтения заказа из файла!");
        }
    }

    @Override
    public void createAll(List<Order> orderList) throws RepositoryException {
        try {
            orderCsv.writeAllToCsv(orderList);
        } catch (IOException e) {
            throw new RepositoryException("Ошибка записи запросов в файл!");
        }

    }
}