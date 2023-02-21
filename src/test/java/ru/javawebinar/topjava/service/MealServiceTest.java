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
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(BREAKFAST_USER_MEAL_ID, USER_ID);
        assertMatch(meal, MealTestData.breakfastUserMeal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(BREAKFAST_USER_MEAL_ID, NOT_FOUND));
    }

    @Test
    public void delete() {
        service.delete(BREAKFAST_USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(BREAKFAST_USER_MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(BREAKFAST_USER_MEAL_ID, NOT_FOUND));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2023, 2, 16), LocalDate.of(2023, 2, 17), USER_ID);
        assertMatch(meals, lunchUserMeal, breakfastUserMeal);
        assertEquals(2, meals.size());
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, dinnerUserMeal, lunchUserMeal, breakfastUserMeal);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(BREAKFAST_USER_MEAL_ID, USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateMealCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, dinnerUserMeal.getDateTime(), "last meal", 10), USER_ID));
    }
}
