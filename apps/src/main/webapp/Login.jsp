<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<link href="./resources/css/yavni/login.css" rel="stylesheet" type="text/css" />
<!-- <link href="/css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" /> --> 
</head>

<body>

<form id="login" action="<c:url value='j_spring_security_check' />" method="post">
    <h1>Log In</h1>
    <fieldset id="inputs">
    	<c:if test="${param.error}">
    		<label style="color: red;">Bad Credentials <%-- ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message} --%></label>
    	</c:if>
        <input id="userName" name="j_username" type="text" placeholder="Username" autofocus required>   
        <input id="password" name="j_password" type="password" placeholder="Password" required>
    </fieldset>
    <fieldset id="actions">
        <input type="submit" id="submit" value="Log in">
        <a href="ForgotPassword.jsp">Forgot your password?</a><a href="">Register</a>
    </fieldset>
</form>

</body>
</html>