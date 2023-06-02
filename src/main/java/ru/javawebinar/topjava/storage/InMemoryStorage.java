package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class InMemoryStorage implements MealsStorage {
    private final List<Meal> userMeals;

    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryStorage(List<Meal> userMeals) {
        this.userMeals = userMeals;
    }

    public InMemoryStorage(List<Meal> userMeals, Integer counter) {
        this.userMeals = userMeals;
        this.counter = new AtomicInteger(counter);
    }

    @Override
    public Meal get(int id) {
        return userMeals.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Meal> getAll() {
        return userMeals;
    }

    @Override
    public void save(Meal userMeal) {
        Integer existingId = userMeal.getId();
        Integer id = existingId == null ? counter.getAndIncrement() : existingId;
        userMeals.add(new Meal(id, userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories()));
    }

    @Override
    public void update(Meal userMeal) {
        int index = IntStream.range(0, userMeals.size())
                .filter(i -> userMeals.get(i).getId().equals(userMeal.getId()))
                .findFirst()
                .orElse(-1);
        if (index >= 0) {
            userMeals.set(index, userMeal);
        }
    }

    @Override
    public void delete(Meal userMeal) {
        userMeals.remove(userMeal);
    }
}
