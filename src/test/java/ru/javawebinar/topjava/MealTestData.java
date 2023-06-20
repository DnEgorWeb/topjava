package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int FIRST_MEAL_ID = START_SEQ + 3;
    public static final Meal MEAL_1 = new Meal(FIRST_MEAL_ID, LocalDateTime.of(2023, 6, 19, 10, 0, 0), "Завтрак", 500);
    public static final Meal MEAL_2 =
            new Meal(FIRST_MEAL_ID + 1, LocalDateTime.of(2023, 6, 19, 13, 0, 0), "Ланч", 1500);
    public static final Meal MEAL_3 = new Meal(FIRST_MEAL_ID + 2, LocalDateTime.of(2023, 6, 19, 19, 0, 0), "Ужин", 800);
    public static final Meal MEAL_4 =
            new Meal(FIRST_MEAL_ID + 3, LocalDateTime.of(2023, 6, 20, 11, 0, 0), "Завтрак", 800);
    public static final Meal MEAL_5 =
            new Meal(FIRST_MEAL_ID + 4, LocalDateTime.of(2023, 6, 20, 15, 0, 0), "Полдник", 300);

    public static Meal getNew() {
        return new Meal(LocalDateTime.now(), "new food", 1200);
    }
}
