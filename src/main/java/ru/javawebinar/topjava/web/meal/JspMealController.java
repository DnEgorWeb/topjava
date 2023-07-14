package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@RequestMapping(value = "/meals")
@Controller
public class JspMealController extends MealController {

    @GetMapping("/{id}")
    public String get(@PathVariable int id, Model model) {
        Meal meal = super.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/{id}/delete")
    public String deleteMeal(@PathVariable int id) {
        super.delete(id);
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
        super.create(meal);
        return "redirect:/meals";
    }

    @PostMapping("/{id}")
    public String update(@RequestParam String dateTime, @RequestParam String description, @RequestParam String calories,
                         @PathVariable int id) {
        Meal meal = new Meal(id, LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        super.update(meal, id);
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String getBetween(@RequestParam(name = "startDate", defaultValue = "") String startDateParam,
                             @RequestParam(name = "startTime", defaultValue = "") String startTimeParam,
                             @RequestParam(name = "endDate", defaultValue = "") String endDateParam,
                             @RequestParam(name = "endTime", defaultValue = "") String endTimeParam, Model model) {
        LocalDate startDate = parseLocalDate(startDateParam);
        LocalDate endDate = parseLocalDate(endDateParam);
        LocalTime startTime = parseLocalTime(startTimeParam);
        LocalTime endTime = parseLocalTime(endTimeParam);
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
