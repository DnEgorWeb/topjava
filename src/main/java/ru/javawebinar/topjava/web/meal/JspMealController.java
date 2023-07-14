package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    @GetMapping("/{id}")
    public String get(@PathVariable int id, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        model.addAttribute("meal", service.get(id, userId));
        return "mealForm";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable int id, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("meal", new Meal());
        return "mealForm";
    }

    @PostMapping("/create")
    public String create(@RequestParam String dateTime, @RequestParam String description,
                         @RequestParam String calories) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        service.create(meal, userId);
        return "redirect:/meals";
    }

    @PostMapping("/{id}")
    public String update(@RequestParam String dateTime, @RequestParam String description, @RequestParam String calories,
                         @PathVariable int id) {
        Meal meal = new Meal(id, LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
        return "redirect:/meals";
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    @GetMapping("/filter")
    public String getBetween(@RequestParam(name = "startDate", defaultValue = "") String startDateParam,
                             @RequestParam(name = "startTime", defaultValue = "") String startTimeParam,
                             @RequestParam(name = "endDate", defaultValue = "") String endDateParam,
                             @RequestParam(name = "endTime", defaultValue = "") String endTimeParam, Model model) {
        LocalDate startDate = parseLocalDate(startDateParam);
        LocalDate endDate = parseLocalDate(endDateParam);
        LocalTime startTime = parseLocalTime(startTimeParam);
        LocalTime endTime = parseLocalTime(endTimeParam);
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        model.addAttribute("meals",
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
