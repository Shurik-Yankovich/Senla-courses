package view.out;

import com.annotation.Singleton;

import java.util.List;

@Singleton
public class StoreViewOut implements ViewOut{

    private static final String MENU_TEXT = "%d - %s";

    @Override
    public void earnedMoneyOut(double money) {
        System.out.println("За данный промежуток времени заработано: " + money);
    }

    @Override
    public void cancelOrderOut(boolean isCanceled) {
        if (isCanceled) {
            System.out.println("Заказ успешно отменен!");
        } else {
            System.out.println("Неудалось отменить заказ!");
        }
    }

    @Override
    public void completeOrderOut(boolean isCompleted) {
        if (isCompleted) {
            System.out.println("Заказ успешно завершен!");
        } else {
            System.out.println("Неудалось завершить заказ!");
        }
    }

    @Override
    public void countCompletedOrderOut(int countOrder) {
        System.out.println("За данный промежуток времени завершено " + countOrder + " заказов");
    }

    @Override
    public void completeRequestOut(boolean isCompleted) {
        if (isCompleted) {
            System.out.println("Запрос успешно выполнен!");
        } else {
            System.out.println("Неудалось выполнить запрос!");
        }
    }

    @Override
    public <T> void printList(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.print(String.format(MENU_TEXT, i, list.get(i)));
        }
    }

    @Override
    public void notFoundMenuItem() {
        System.err.println("Неверно выбран пункт меню!\nПожалуйста выбирете нужный пункт меню:\n");
    }

    @Override
    public void printExceptionMessage(String text) {
        System.err.println(text);
    }

    @Override
    public void readOrderListFromFile(){
        System.out.println("Список заказов был считан из файла");
    }

    @Override
    public void readRequestListFromFile(){
        System.out.println("Список запросов был считан из файла");
    }

    @Override
    public void readBookshelfListFromFile(){
        System.out.println("Список книг был считан из файла");
    }

    @Override
    public void writeOrderListFromFile(){
        System.out.println("Список заказов был записан в файл");
    }

    @Override
    public void writeRequestListFromFile(){
        System.out.println("Список запросов был записан в файл");
    }

    @Override
    public void writeBookshelfListFromFile(){
        System.out.println("Список книг был записан в файл");
    }
}