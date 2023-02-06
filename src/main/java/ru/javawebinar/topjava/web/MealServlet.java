package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.repository.MealRepositoryImpl.getRandomInt;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static String INSERT_OR_EDIT = "/edit.jsp";
    private static String LIST_USER = "/meals.jsp";
    private MealRepository mealRepository;
    private static final Logger log = getLogger(MealServlet.class);
    public static final int caloriesPerDay = 2000;

    public MealServlet() {
        super();
        mealRepository = new MealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward = "";
        String action = request.getParameter("action");

        if (action == null) {
            List<MealTo> mealTos = filteredByStreams(mealRepository.getMeals(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
            request.setAttribute("meals", mealTos);
            log.debug("forward to meals");
            getServletContext().getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("id"));
            List<MealTo> mealTos = filteredByStreams(mealRepository.getMeals(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
            List<MealTo> mealTosAfterDelete = mealRepository.deleteMeal(mealTos, mealId);
            forward = LIST_USER;
            request.setAttribute("meals", mealTosAfterDelete);
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("id"));
            Meal meal = mealRepository.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeal")) {
            forward = LIST_USER;
            request.setAttribute("meals", mealRepository.getAllMeals(mealRepository.getMeals()));
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        request.setCharacterEncoding("UTF-8");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Date dob = new SimpleDateFormat("MM/dd/yyyy hh:mm").parse(request.getParameter("dateTime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Meal meal = new Meal(LocalDateTime.now(), request.getParameter("description"), Integer.valueOf(request.getParameter("calories")), getRandomInt());
        String mealId = request.getParameter("id");
        if (mealId == null || mealId.isEmpty()) {
            mealRepository.addMeal(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            mealRepository.updateMeal(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("meals", mealRepository.getAllMeals(mealRepository.getMeals()));
        request.setCharacterEncoding("UTF-8");
        view.forward(request, response);
    }
}
