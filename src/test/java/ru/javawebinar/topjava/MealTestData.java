package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int BREAKFAST_USER_MEAL_ID = START_SEQ + 3;
    public static final int LUNCH_USER_MEAL_ID = BREAKFAST_USER_MEAL_ID + 1;
    public static final int DINNER_USER_MEAL_ID = LUNCH_USER_MEAL_ID + 1;

    public static final Meal breakfastUserMeal = new Meal(BREAKFAST_USER_MEAL_ID, LocalDateTime.of(2023, 2, 16, 9, 0), "breakfast only", 1000);
    public static final Meal lunchUserMeal = new Meal(LUNCH_USER_MEAL_ID, LocalDateTime.of(2023, 2, 17, 12, 0), "lunch only", 1000);
    public static final Meal dinnerUserMeal = new Meal(DINNER_USER_MEAL_ID, LocalDateTime.of(2023, 2, 18, 17, 0), "dinner only", 1000);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, 2, 17, 17, 0), "new meal", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(breakfastUserMeal);
        updated.setDateTime(LocalDateTime.of(2023, 2, 17, 9, 0));
        updated.setCalories(100);
        updated.setDescription("updated meal");
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
