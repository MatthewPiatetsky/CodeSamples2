<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<link rel="stylesheet" href="css/style.css" />

<title>Logout</title></head>

<body>
<h2>Logged out</h2>

<% session.invalidate(); %>
<a href="/photoshare/index.jsp">Login again</a>
</body>
</html>
