<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Meal</title>
  <link href="styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<form class="form" method="post">
  <label for="dateTime">Date</label>
  <input id="dateTime" name="dateTime" required value="" type="datetime-local">
  <label for="description">Description</label>
  <input id="description" name="description" required value="">
  <label for="calories">Calories</label>
  <input id="calories" name="calories" required value="">
  <br />
  <button>Create</button>
</form>
</body>
</html>
