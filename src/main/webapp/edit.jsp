<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <meta charset="UTF-8"/>
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>

<script>
    $(function () {
        $('input[name=dateTime]').datepicker();
    });
</script>

<form method="POST" action='meals?id=<c:out value="${meal.id}"/>' name="frmAddMeal">
    DateTime : <input
        type="datetime" name="dateTime" value="${meal.dateTime}"/>
    <br/>

    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />"/>
    <br/>

    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.calories}" />"/>
    <br/>
    <br/>
    <input type="submit" value="Submit"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>

</body>
</html>
