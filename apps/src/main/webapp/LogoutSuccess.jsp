<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Logged out Successfully</title>
<script>
	window.setTimeout(function() {
		location.href = document.getElementById("link").href;
		}, 2000
	);
</script>
</head>
<body>
Logged out Successfully, will be redirecting to Login page
<a id="link" href="/Login.jsp">Login</a>
</body>
</html>