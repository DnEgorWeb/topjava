package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals =
                Arrays.asList(new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println();
        List<UserMealWithExcess> mealsToByStreams =
                filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        mealsToByStreams.forEach(System.out::println);
        List<UserMealWithExcess> singleCycleMealsTo =
                filteredBySingleCycle(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        singleCycleMealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            int caloriesWithMeal = caloriesSumByDate.getOrDefault(mealDate, 0) + meal.getCalories();
            caloriesSumByDate.put(mealDate, caloriesWithMeal);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)), caloriesSumByDate -> meals.stream()
                        .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime,
                                endTime))
                        .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                                meal.getCalories(),
                                caloriesSumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                        .collect(Collectors.toList())));
    }

    public static List<UserMealWithExcess> filteredBySingleCycle(List<UserMeal> meals, LocalTime startTime,
                                                                 LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        Map<LocalDate, List<Map.Entry<Integer, UserMeal>>> mealEntriesByDate = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            boolean caloriesExceeded = caloriesSumByDate.getOrDefault(mealDate, 0) > caloriesPerDay;
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesExceeded));
                List<Map.Entry<Integer, UserMeal>> mealIndexes =
                        mealEntriesByDate.getOrDefault(mealDate, new ArrayList<>());
                mealIndexes.add(new AbstractMap.SimpleEntry<>(result.size() - 1, meal));
                mealEntriesByDate.put(mealDate, mealIndexes);
            }
            caloriesSumByDate.merge(mealDate, meal.getCalories(), Integer::sum);
            boolean caloriesWithMealExceeded = caloriesSumByDate.get(mealDate) > caloriesPerDay;
            if (caloriesWithMealExceeded && !caloriesExceeded) {
                mealEntriesByDate.get(mealDate).forEach(entry -> {
                    UserMeal m = entry.getValue();
                    result.set(entry.getKey(),
                            new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), true));
                });
            }
        }
        return result;
    }
}
