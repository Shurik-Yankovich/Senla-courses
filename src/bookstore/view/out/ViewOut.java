package bookstore.view.out;

import java.util.List;

public interface ViewOut {

    void earnedMoneyOut(double money);
    void cancelOrderOut(boolean isCanceled);
    void completeOrderOut(boolean isCompleted);
    void countCompletedOrderOut(int countOrder);
    <T> void printList(List<T> list);
    void notFoundMenuItem();
}