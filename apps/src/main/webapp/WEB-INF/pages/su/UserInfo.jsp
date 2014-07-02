
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/Head.jsp" %> 
</head>

<body>
	<div class="wrapper">
		<div class="main_body">
			<%@ include file="/LeftPanel.jsp" %>
			
			<div class="center_bg1">
				<div class="lm_01">
					<div id="style_00">User - ${userInfo.firstName}'s Information</div>
				</div>
				<div class="center_021">
					<table>
							<tr>
								<td>User Name: </td><td>${userInfo.userName}</td>
							</tr>
							<tr>
								<td>First Name: </td><td>${userInfo.firstName}</td>
							</tr>
							<tr>
								<td>Last Name: </td><td>${userInfo.lastName}</td>
							</tr>
							<tr>
								<td>Role: </td><td>${userInfo.roleStr}</td>
							</tr>							
						</table>

					<br />
					<div id="style_011">
						<p>&nbsp;</p>
					</div>
				</div>
			</div>
		</div>

		<%@ include file="../../../Footer.jsp" %> 
	</div>
</body>
</html>
