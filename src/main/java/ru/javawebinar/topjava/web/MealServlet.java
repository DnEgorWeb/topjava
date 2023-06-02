package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.InMemoryStorage;
import ru.javawebinar.topjava.storage.MealsStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private final MealsStorage mealsStorage = new InMemoryStorage(MealsUtil.getMeals(), MealsUtil.getMeals().size());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            List<Meal> meals = mealsStorage.getAll();
            req.setAttribute("meals", MealsUtil.getMealsWithExcess(meals, MealsUtil.CALORIE_LIMIT));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        } else if (action.equals("update")) {
            String id = req.getParameter("id");
            Meal userMeal = mealsStorage.get(Integer.parseInt(id));
            req.setAttribute("meal", userMeal);
            req.getRequestDispatcher("/mealUpdate.jsp").forward(req, resp);
        } else if (action.equals("create")) {
            req.getRequestDispatcher("/mealCreate.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            String id = req.getParameter("id");
            Meal userMeal = mealsStorage.get(Integer.parseInt(id));
            mealsStorage.delete(userMeal);
            resp.sendRedirect("/topjava/meals");
        } else {
            throw new IllegalArgumentException("Unsupported action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        if (action.equals("create")) {
            Meal meal = new Meal(null, dateTime, description, calories);
            mealsStorage.save(meal);
        } else if (action.equals("update")) {
            Meal meal = new Meal(Integer.parseInt(req.getParameter("id")), dateTime, description, calories);
            mealsStorage.update(meal);
        } else {
            throw new IllegalArgumentException("Unsupported action");
        }
        resp.sendRedirect("/topjava/meals");
    }
}
