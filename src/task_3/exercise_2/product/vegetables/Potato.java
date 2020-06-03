package task_3.exercise_2.product.vegetables;

import task_3.exercise_2.product.Product;

public class Potato implements Product {

    private String name;
    private double weight;

    public Potato(double weight) {
        this.weight = weight;
        name = "Картофель";
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Продукт: [%s] имеет вес: %.3f кг", getName(), getWeight());
    }
}
