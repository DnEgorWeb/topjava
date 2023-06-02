package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsStorage {
    Meal get(int id);

    List<Meal> getAll();

    Meal create(Meal userMeal);

    Meal update(Meal userMeal);

    void delete(int id);
}
