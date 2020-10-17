package repository.file;

import entity.Request;
import exeption.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import repository.base.RequestRepository;
import util.csv.RequestCsv;

import java.io.IOException;
import java.util.List;

@Repository
public class FileRequestRepository implements RequestRepository {

    @Autowired
    private RequestCsv requestCsv;

    @Override
    public Request create(Request request) throws RepositoryException {
        try {
            List<Request> requestList = requestCsv.readAllFromCsv();
            for (Request bookRequest : requestList) {
                if (bookRequest.getId() == request.getId()) {
                    request.setId(requestList.size());
                }
            }
            requestCsv.writeToCsv(request);
            return request;
        } catch (IOException e) {
            throw new RepositoryException("Ошибка записи запроса в файл!");
        }
    }

    @Override
    public Request update(Request request) throws RepositoryException {
        try {
            List<Request> requestList = requestCsv.readAllFromCsv();
            boolean isPresent = false;
            for (int i = 0; i < requestList.size(); i++) {
                if (requestList.get(i).getId() == request.getId()) {
                    requestList.set(i, request);
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) {
                requestList.add(request);
            }
            requestCsv.writeAllToCsv(requestList);
            return request;
        } catch (IOException e) {
            throw new RepositoryException("Ошибка обновления запроса в файле!");
        }
    }

    @Override
    public Request read(Integer id) throws RepositoryException {
        try {
            return requestCsv.readFromCsv(id);
        } catch (IOException e) {
            throw new RepositoryException("Ошибка чтения запроса из файла!");
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public List<Request> readAll() throws RepositoryException {
        try {
            return requestCsv.readAllFromCsv();
        } catch (IOException e) {
            throw new RepositoryException("Ошибка чтения запросов из файлае!");
        }
    }

    @Override
    public void createAll(List<Request> requestList) throws RepositoryException {
        try {
            requestCsv.writeAllToCsv(requestList);
        } catch (IOException e) {
            throw new RepositoryException("Ошибка записи запросов в файл!");
        }

    }
}