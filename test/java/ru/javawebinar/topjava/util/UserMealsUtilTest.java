package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.util.UserMealsUtil.filteredByCycles;
import static ru.javawebinar.topjava.util.UserMealsUtil.filteredByStreams;

public class UserMealsUtilTest {

    private final List<UserMeal> meals = Arrays.asList(
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

    @Test
    public void testFilteredByCycles() {
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        assertEquals(2, mealsTo.size());
        assertFalse(mealsTo.get(0).getExcess());
        assertTrue(mealsTo.get(1).getExcess());
    }

    @Test
    public void testFilteredByStreams() {
        List<UserMealWithExcess> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        assertEquals(2, mealsTo.size());
        assertFalse(mealsTo.get(0).getExcess());
        assertTrue(mealsTo.get(1).getExcess());
    }
}
