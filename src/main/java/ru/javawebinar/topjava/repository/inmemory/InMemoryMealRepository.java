package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(m, m.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
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
    public List<Meal> getAll(int userId) {
        return getList(userId, m -> true);
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate) {
        return getList(userId, m -> DateTimeUtil.isBetweenHalfOpen(m.getDate(), startDate, endDate));
    }

    private List<Meal> getList(int userId, Predicate<Meal> condition) {
        return repository.values()
                .stream()
                .filter(m -> m.getUserId() == userId)
                .filter(condition)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private <T> T checkBelongsToUser(int id, int userId, T negativeResult, Function<Meal, T> callback) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId) {
            return negativeResult;
        }
        return callback.apply(meal);
    }
}

