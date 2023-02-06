package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealRepository {
    void addMeal(Meal meal);

    void updateMeal(Meal meal);

    List<MealTo> deleteMeal(List<MealTo> mealTos, Integer mealId);

    List<MealTo> getAllMeals(List<Meal> meals);

    Meal getMealById(int mealId);

    List<Meal> getMeals();
}
