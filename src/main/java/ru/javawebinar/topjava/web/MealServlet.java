package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealInMemoryStorage;
import ru.javawebinar.topjava.storage.MealsStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private final MealsStorage mealsStorage = new MealInMemoryStorage();

    @Override
    public void init() {
        mealsStorage.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealsStorage.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealsStorage.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealsStorage.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealsStorage.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealsStorage.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealsStorage.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            getMeals(req, resp);
            return;
        }
        switch (action) {
            case "update":
                log.debug("GET update meal");
                String id = req.getParameter("id");
                Meal userMeal = mealsStorage.get(Integer.parseInt(id));
                req.setAttribute("meal", userMeal);
                req.getRequestDispatcher("/mealUpdate.jsp").forward(req, resp);
                break;
            case "create":
                log.debug("GET create meal");
                Meal meal = new Meal(null, LocalDateTime.now(), "", 0);
                req.setAttribute("meal", meal);
                req.getRequestDispatcher("/mealUpdate.jsp").forward(req, resp);
                break;
            case "delete":
                log.debug("GET delete meal");
                mealsStorage.delete(Integer.parseInt(req.getParameter("id")));
                redirectToMeals(resp);
                break;
            default:
                getMeals(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String idParam = req.getParameter("id");
        if (idParam.equals("")) {
            log.debug("POST save meal");
            mealsStorage.create(new Meal(null, dateTime, description, calories));
        } else {
            log.debug("POST update meal");
            mealsStorage.update(new Meal(Integer.parseInt(req.getParameter("id")), dateTime, description, calories));
        }
        redirectToMeals(resp);
    }

    private void getMeals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET all meals");
        List<Meal> meals = mealsStorage.getAll();
        req.setAttribute("meals", MealsUtil.getMealsWithExcess(meals, MealsUtil.CALORIE_LIMIT));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private void redirectToMeals(HttpServletResponse resp) throws IOException {
        log.debug("redirect to meals");
        resp.sendRedirect("meals");
    }
}
