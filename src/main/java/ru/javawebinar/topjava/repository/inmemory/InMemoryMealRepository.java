package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        return checkBelongsToUser(meal.getId(), meal.getUserId(), null,
                (ignored) -> repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal));
    }

    @Override
    public boolean delete(int id, int userId) {
        return checkBelongsToUser(id, userId, false, (meal) -> repository.remove(id) != null);
    }

    @Override
    public Meal get(int id, int userId) {
        return checkBelongsToUser(id, userId, null, (meal) -> meal);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values()
                .stream()
                .filter(m -> m.getUserId() == userId)
                .sorted((a, b) -> b.getDateTime().compareTo(a.getDateTime()))
                .collect(Collectors.toList());
    }

    private <T> T checkBelongsToUser(int id, int userId, T negativeResult, Function<Meal, T> cb) {
        Meal meal = repository.get(id);
        if (meal.getUserId() == userId) {
            return cb.apply(meal);
        }
        return negativeResult;
    }
}

