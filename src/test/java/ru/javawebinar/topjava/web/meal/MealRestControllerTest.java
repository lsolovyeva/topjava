package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/meals/" + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/meals/" + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/meals"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meals));
    }

    @Test
    void create() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/rest/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put("/rest/meals/" + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void getBetween() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 23, 59, 59);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startDate = startDateTime.format(fmt);
        String endDate = endDateTime.format(fmt);

        perform(MockMvcRequestBuilders.get("/rest/meals/between")
                // + "?startDateTime=" + startDate+ "?endDateTime=" + endDate
                .param("startDateTime", startDate)
                .param("endDateTime", endDate)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal3, meal2, meal1));
    }

    @Test
    void getBetweenOptional() throws Exception {
        LocalDate startDate = LocalDate.of(2020, Month.JANUARY, 30);
        LocalDate endDate = LocalDate.of(2020, Month.JANUARY, 30);
        LocalTime startTime = LocalTime.of(0, 0, 0);
        LocalTime endTime = LocalTime.of(23, 59, 59);
        DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");

        perform(MockMvcRequestBuilders.get("/rest/meals/optional/between")
                .param("startDate", startDate.format(fmtDate))
                .param("startTime", startTime.format(fmtTime))
                .param("endDate", endDate.format(fmtDate))
                .param("endTime", endTime.format(fmtTime))

        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal3, meal2, meal1));
    }
}