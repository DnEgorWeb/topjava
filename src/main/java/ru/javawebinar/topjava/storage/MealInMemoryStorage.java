package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealInMemoryStorage implements MealsStorage {
    private final Map<Integer, Meal> userMeals = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal get(int id) {
        return userMeals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(userMeals.values());
    }

    @Override
    public Meal create(Meal userMeal) {
        userMeal.setId(counter.getAndIncrement());
        return userMeals.put(userMeal.getId(), userMeal);
    }

    @Override
    public Meal update(Meal userMeal) {
        if (userMeals.containsKey(userMeal.getId())) {
            return userMeals.put(userMeal.getId(), userMeal);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        userMeals.remove(id);
    }
}
