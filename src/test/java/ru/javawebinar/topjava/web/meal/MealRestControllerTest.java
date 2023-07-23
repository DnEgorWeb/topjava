package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(MealsUtil.getTos(meals, DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID)).andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void create() throws Exception {
        Meal meal = MealTestData.getNew();
        var jsonEncodedMeal = JsonUtil.writeValue(meal);
        perform(MockMvcRequestBuilders.post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(jsonEncodedMeal)).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void update() throws Exception {
        Meal meal = mealService.create(MealTestData.getNew(), USER_ID);
        var jsonEncodedMeal = JsonUtil.writeValue(meal);
        perform(MockMvcRequestBuilders.put(REST_URL + meal.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(jsonEncodedMeal)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getBetween() throws Exception {
        String requestURL = REST_URL + "filter?" + "startDateTime=" +
                meal4.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "&endDateTime=" +
                meal7.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        perform(MockMvcRequestBuilders.get(requestURL)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(Stream.of(meal6, meal5, meal4)
                        .map(m -> new MealTo(m.getId(), m.getDateTime(), m.getDescription(), m.getCalories(), true))
                        .toList()));
    }
}
