package repository.list;

import entity.Request;
import repository.base.RequestRepository;

import java.util.List;

public class BookRequestRepository implements RequestRepository {

    private List<Request> array;

    @Override
    public String toString() {
        return array.toString();
    }

    @Override
    public Request create(Request request) {
        int index = array.size();
        request.setId(index);
        array.add(request);
        return request;
    }

    @Override
    public Request update(Request request) {
        int index = request.getId();
        if (array.get(index) != null) {
            array.set(index, request);
            return request;
        }
        return null;
    }

    @Override
    public Request read(Integer requestNumber) {
        Request request = null;
        if (requestNumber >= 0 && requestNumber < array.size()) {
            request = searchByRequestNumber(requestNumber);
        }
        return request;
    }

    @Override
    public void delete(Integer requestNumber) {
        if (requestNumber >= 0 && requestNumber < array.size()) {
            Request request = searchByRequestNumber(requestNumber);
            array.remove(request);
        }
    }

    @Override
    public void createAll(List<Request> requests) {
        array = requests;
    }

    private Request searchByRequestNumber(int requestNumber) {
        for (Request request : array) {
            if (request.getId() == requestNumber) {
                return request;
            }
        }
        return null;
    }

    @Override
    public List<Request> readAll() {
        return array;
    }
}