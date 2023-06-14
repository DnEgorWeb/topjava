package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Role;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static int userId = Role.USER.ordinal();

    public static int authUserId() {
        return userId;
    }

    public static void setAuthUserId(int userId) {
        SecurityUtil.userId = userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}