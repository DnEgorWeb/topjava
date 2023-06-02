<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
    <link href="styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<form class="form" method="post">
    <input type="hidden" name="id" value="${meal.id}" />
    <label for="dateTime">Date</label>
    <input id="dateTime" name="dateTime" value="${meal.dateTime}" type="datetime-local" readonly>
    <label for="description">Description</label>
    <input id="description" name="description" value="${meal.description}">
    <label for="calories">Calories</label>
    <input id="calories" name="calories" value="${meal.calories}">
    <br/>
    <button>Update</button>
</form>
</body>
</html>
