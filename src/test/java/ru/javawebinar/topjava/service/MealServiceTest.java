package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_1;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        assertEquals(created, service.get(created.getId(), USER_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(MEAL_1.getDateTime(), MEAL_1.getDescription(), MEAL_1.getCalories()), USER_ID));
    }

    @Test
    public void delete() {
        Meal created = service.create(getNew(), USER_ID);
        service.delete(created.getId(), USER_ID);
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_1.getId(), USER_ID * 2));
    }

    @Test
    public void get() {
        Meal created = service.create(getNew(), USER_ID);
        assertEquals(created, service.get(created.getId(), USER_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_1.getId(), USER_ID * 2));
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL_1.getId(), MEAL_1.getDateTime(), MEAL_1.getDescription(), MEAL_1.getCalories());
        updated.setDescription("Updated: " + updated.getDescription());
        updated.setCalories(updated.getCalories() + 1);
        service.update(updated, USER_ID);
        assertEquals(updated, service.get(updated.getId(), USER_ID));
    }

    @Test
    public void updateNotFound() {
        Meal updated = new Meal(MEAL_1.getId(), MEAL_1.getDateTime(), MEAL_1.getDescription(), MEAL_1.getCalories());
        updated.setDescription("Updated: " + updated.getDescription());
        updated.setCalories(updated.getCalories() + 1);
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID * 2));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        all.forEach(meal -> assertEquals(meal, service.get(meal.getId(), USER_ID)));
    }
}
