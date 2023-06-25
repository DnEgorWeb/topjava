package ru.javawebinar.topjava.repository.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryTest {
    @Autowired
    private MealRepository repository;

    @Test
    public void get() {
        Assert.assertEquals("returns meal", meal1, repository.get(meal1.getId(), USER_ID));
    }

    @Test
    public void getReturnsNullWhenRequestingStrangerMeal() {
        Assert.assertNull("returns null when requested meal does not belong to the user",
                repository.get(meal1.getId(), ADMIN_ID));
    }

    @Test
    public void getReturnsNullWhenNoSuchMeal() {
        Assert.assertNull("returns null when no such meal exists", repository.get(MEAL1_ID - 1, USER_ID));
    }

    @Test
    public void getAll() {
        MEAL_MATCHER.assertMatch(meals, repository.getAll(USER_ID));
    }

    @Test
    public void getBetweenHalfOpen() {
        LocalDateTime start = LocalDateTime.of(2020, Month.JANUARY, 30, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0);
        MEAL_MATCHER.assertMatch(Arrays.asList(meal1, meal2, meal3),
                repository.getBetweenHalfOpen(start, end, USER_ID));
    }

    @Test
    public void save() {
        Meal created = new Meal(LocalDateTime.now(), "mock", 2000);
        repository.save(created, USER_ID);
        Meal retrieved = repository.get(created.getId(), USER_ID);
        Assert.assertEquals(created, retrieved);
    }

    @Test
    public void delete() {
        assertTrue("returns true when deleted entity", repository.delete(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotExistingMeal() {
        assertFalse("returns false when attempts to delete non existing meal", repository.delete(MEAL1_ID - 1, USER_ID));
    }

    @Test
    public void deleteStrangerMeal() {
        assertFalse("returns false when attempts to delete stranger meal", repository.delete(MEAL1_ID, ADMIN_ID));
    }
}
