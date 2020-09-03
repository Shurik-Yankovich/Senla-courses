package bookstore.repository.db;

import bookstore.constant.SqlConstant;
import bookstore.entity.Book;
import bookstore.entity.Customer;
import bookstore.entity.Order;
import bookstore.entity.Status;
import bookstore.exeption.RepositoryException;
import bookstore.repository.base.OrderRepository;
import bookstore.util.connections.ConnectionUtils;
import com.annotation.InjectByType;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DBOrderRepository implements OrderRepository {

    @InjectByType
    private ConnectionUtils connectionUtils;

    @Override
    public Order create(Order order) throws RepositoryException {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = connectionUtils.getConnection();
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint("OrderRepository.create." + LocalDateTime.now().toString());
            order = addOrderToDB(connection, order);
            connection.commit();
        } catch (SQLException e) {
            try {
                order = null;
                connection.rollback(savepoint); //откат к точке сохранения
                connection.releaseSavepoint(savepoint); //высвободить ранее определённую точку сохранения
                throw new RepositoryException("Неудалось завершить транзакцию добавления заказа в бд!");
            } catch (SQLException ex) {
                throw new RepositoryException("Неудалось отменить транзакцию добавления заказа в бд!");
            }
        } finally {
            try {
                connectionUtils.closeConnection();
            } catch (SQLException e) {
                throw new RepositoryException("Неудалось закрыть соединение с бд!");
            }
        }
        return order;
    }

    @Override
    public Order update(Order order) throws RepositoryException {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = connectionUtils.getConnection();
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint("OrderRepository.update." + LocalDateTime.now().toString());
            PreparedStatement preparedStatement = connection.prepareStatement(SqlConstant.UPDATE_ORDER);
            preparedStatement.setDate(1, Date.valueOf(order.getOrderCompletionDate()));
            preparedStatement.setDouble(2, order.getPrice());
            preparedStatement.setString(3, order.getStatus().toString());
            preparedStatement.setInt(4, order.getId());
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            try {
                order = null;
                connection.rollback(savepoint); //откат к точке сохранения
                connection.releaseSavepoint(savepoint); //высвободить ранее определённую точку сохранения
                throw new RepositoryException("Неудалось завершить транзакцию обновления заказа в бд!");
            } catch (SQLException ex) {
                throw new RepositoryException("Неудалось отменить транзакцию обновления заказа в бд!");
            }
        } finally {
            try {
                connectionUtils.closeConnection();
            } catch (SQLException e) {
                throw new RepositoryException("Неудалось закрыть соединение с бд!");
            }
        }
        return order;
    }

    @Override
    public Order read(Integer primaryKey) throws RepositoryException {
        Connection connection = null;
        Savepoint savepoint = null;
        Order order = null;
        try {
            connection = connectionUtils.getConnection();
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint("OrderRepository.read." + LocalDateTime.now().toString());
            PreparedStatement preparedStatement = connection.prepareStatement(SqlConstant.READ_ORDER);
            preparedStatement.setInt(1, primaryKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            order = getOrderOnResultSet(connection, resultSet);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback(savepoint); //откат к точке сохранения
                connection.releaseSavepoint(savepoint); //высвободить ранее определённую точку сохранения
                throw new RepositoryException("Неудалось завершить транзакцию чтения заказа из бд!");
            } catch (SQLException ex) {
                throw new RepositoryException("Неудалось отменить транзакцию чтения заказа из бд!");
            }
        } finally {
            try {
                connectionUtils.closeConnection();
            } catch (SQLException e) {
                throw new RepositoryException("Неудалось закрыть соединение с бд!");
            }
        }
        return order;
    }

    @Override
    public void delete(Integer primaryKey) throws RepositoryException {

    }

    @Override
    public List<Order> readAll() throws RepositoryException {
        Connection connection = null;
        Savepoint savepoint = null;
        List<Order> orderList = new ArrayList<>();
        try {
            connection = connectionUtils.getConnection();
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint("OrderRepository.readAll." + LocalDateTime.now().toString());
            PreparedStatement preparedStatement = connection.prepareStatement(SqlConstant.READ_ALL_ORDER);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderList.add(getOrderOnResultSet(connection, resultSet));
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                orderList = null;
                connection.rollback(savepoint); //откат к точке сохранения
                connection.releaseSavepoint(savepoint); //высвободить ранее определённую точку сохранения
                throw new RepositoryException("Неудалось завершить транзакцию чтения всех заказов из бд!");
            } catch (SQLException ex) {
                throw new RepositoryException("Неудалось отменить транзакцию чтения всех заказов из бд!");
            }
        } finally {
            try {
                connectionUtils.closeConnection();
            } catch (SQLException e) {
                throw new RepositoryException("Неудалось закрыть соединение с бд!");
            }
        }
        return orderList;
    }

    @Override
    public void createAll(List<Order> orderList) throws RepositoryException {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = connectionUtils.getConnection();
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint("OrderRepository.createAll." + LocalDateTime.now().toString());
            for (Order order : orderList) {
                addOrderToDB(connection, order);
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback(savepoint);
                connection.releaseSavepoint(savepoint);
                throw new RepositoryException("Неудалось завершить транзакцию добавления заказов в бд!");
            } catch (SQLException ex) {
                throw new RepositoryException("Неудалось отменить транзакцию добавления заказов в бд!");
            }
        } finally {
            try {
                connectionUtils.closeConnection();
            } catch (SQLException e) {
                throw new RepositoryException("Неудалось закрыть соединение с бд!");
            }
        }
    }

    private Order addOrderToDB(Connection connection, Order order) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SqlConstant.CREATE_ORDER);
        int orderId = order.getId();
        preparedStatement.setInt(1, orderId);
        preparedStatement.setString(2, order.getCustomer().getFullName());
        preparedStatement.setString(3, order.getCustomer().getAddress());
        preparedStatement.setString(4, order.getCustomer().getPhoneNumber());
        preparedStatement.setDate(5, Date.valueOf(order.getOrderDate()));
        preparedStatement.setDate(6, Date.valueOf(order.getOrderCompletionDate()));
        preparedStatement.setDouble(7, order.getPrice());
        preparedStatement.setString(8, order.getStatus().toString());
        preparedStatement.execute();
        //--------------------------
        for (Integer requestId : order.getNumbersRequest()) {
            preparedStatement = connection.prepareStatement(SqlConstant.CREATE_REQUEST_BY_ORDER);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, requestId);
            preparedStatement.execute();
        }
        //--------------------------
        for (Book book : order.getBooks()) {
            preparedStatement = connection.prepareStatement(SqlConstant.CREATE_BOOK_BY_ORDER);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, book.getId());
            preparedStatement.execute();
        }
        return order;
    }

    private Order getOrderOnResultSet(Connection connection, ResultSet rs) throws SQLException {
        Order order = new Order();
        Customer customer = new Customer();
        int orderId = rs.getInt("id");
        order.setId(orderId);
        customer.setFullName(rs.getString("fullName"));
        customer.setAddress(rs.getString("address"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        order.setCustomer(customer);
        order.setOrderDate(convertDateToLocalDate(rs.getDate("order_date")));
        order.setOrderCompletionDate(convertDateToLocalDate(rs.getDate("completion_date")));
        order.setPrice(rs.getDouble("price"));
        order.setStatus(Status.valueOf(rs.getString("status")));
        //получаем список запросов для заказа
        PreparedStatement preparedStatement = connection.prepareStatement(SqlConstant.READ_REQUEST_BY_ORDER);
        preparedStatement.setInt(1, orderId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Integer> numbersRequest = new ArrayList<>();
        while (resultSet.next()) {
            numbersRequest.add(resultSet.getInt(1));
        }
        order.setNumbersRequest(numbersRequest);
        //получаем список книг для заказа
        preparedStatement = connection.prepareStatement(SqlConstant.READ_BOOK_BY_ORDER);
        preparedStatement.setInt(1, orderId);
        resultSet = preparedStatement.executeQuery();
        List<Integer> numbersBooks = new ArrayList<>();
        while (resultSet.next()) {
            numbersBooks.add(resultSet.getInt(1));
        }
        List<Book> books = new ArrayList<>();
        for (Integer bookId : numbersBooks) {
            preparedStatement = connection.prepareStatement(SqlConstant.READ_BOOK);
            preparedStatement.setInt(1, bookId);
            resultSet = preparedStatement.executeQuery();
            Book book = new Book();
            book.setId(resultSet.getInt("id"));
            book.setTitle(resultSet.getString("title"));
            book.setAuthor(resultSet.getString("author"));
            book.setPublicationYear(resultSet.getInt("publicationYear"));
            books.add(book);
        }
        order.setBooks(books);
        return order;
    }

    private LocalDate convertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}