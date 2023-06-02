package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsStorage {
    Meal get(int id);

    List<Meal> getAll();

    void save(Meal userMeal);

    void update(Meal userMeal);

    void delete(Meal userMeal);
}
