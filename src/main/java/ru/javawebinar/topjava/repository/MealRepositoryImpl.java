package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;
import static ru.javawebinar.topjava.web.MealServlet.caloriesPerDay;

public class MealRepositoryImpl implements MealRepository {
    public List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, 1),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, 2),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, 3),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, 4),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, 5),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500, 6),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, 7)
    );

    public MealRepositoryImpl() {
    }

    private void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public List<Meal> getMeals() {
        return meals;
    }

    public static Integer getRandomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    @Override
    public void addMeal(Meal meal) {
        List<Meal> mealsWithNewMeal = new ArrayList<>(meals);
        meal.setId(getRandomInt());
        mealsWithNewMeal.add(meal);
        this.meals = mealsWithNewMeal;
    }

    @Override
    public void updateMeal(Meal meal) {
        Meal mealToDelete = getMealById(meal.getId());
        List<Meal> meals = new ArrayList<>(this.meals);
        meals.remove(mealToDelete);
        meals.add(meal);
        setMeals(meals);
    }

    @Override
    public List<MealTo> deleteMeal(List<MealTo> mealTos, Integer mealId) {
        List<MealTo> res = new ArrayList<>(mealTos);
        MealTo mealToDelete = mealTos.stream().filter(m -> Objects.equals(mealId, m.getId())).findFirst().orElse(null);
        Meal mealDelete = this.meals.stream().filter(m -> Objects.equals(mealId, m.getId())).findFirst().orElse(null);
        List<Meal> meals = new ArrayList<>(this.meals);
        meals.remove(mealDelete);
        setMeals(meals);
        res.remove(mealToDelete);
        return res;
    }

    @Override
    public List<MealTo> getAllMeals(List<Meal> meals) {
        return filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    @Override
    public Meal getMealById(int mealId) {
        return meals.stream().filter(m -> mealId == m.getId()).findFirst().orElse(null);
    }
}
