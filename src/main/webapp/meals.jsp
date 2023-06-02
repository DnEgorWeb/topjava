<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <link href="styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>-</th>
        <th>-</th>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr class="${meal.excess ? "danger-bg" : ""}">
            <td class="date-time">${meal.dateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="?action=update&id=${meal.id}">Update</a></td>
            <td><a href="?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<br />
<a href="?action=create">
    <button class="button">Add a meal</button>
</a>
<script type="text/javascript">
    document.querySelectorAll(".date-time").forEach(node => {
        node.innerHTML = formatDate(node.innerHTML)
    })

    function formatDate(isoDateString) {
        const dateObject = new Date(isoDateString)
        const year = dateObject.getFullYear()
        const month = ("0" + (dateObject.getMonth() + 1)).slice(-2)
        const day = ("0" + dateObject.getDate()).slice(-2)
        const hours = ("0" + dateObject.getHours()).slice(-2)
        const minutes = ("0" + dateObject.getMinutes()).slice(-2)
        return year + "-" + month + "-" + day + " " + hours + ":" + minutes
    }
</script>
</body>
</html>
