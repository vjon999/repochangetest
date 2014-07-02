
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/Head.jsp" %> 
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js">
</script>
<script>
$(document).ready(function(){
  $("button").click(function(){
    $("#div1").load("demo_test.txt");
  });
});
</script>

</head>

<body>
	<div class="wrapper">		
		<div class="main_body">
			<%@ include file="../../../LeftPanel.jsp" %> 
			
			<div class="center_bg1">
				<div class="lm_01">
					<div id="style_00">Create User</div>
				</div>
				<div class="center_021">
					<form action="/mvc/su/saveUser" method="post">
						<label id="errorMsg">${errorMsg}</label>
						<table>
							<tr>
								<td>User Name:</td>
								<td><input type="text" name="userName" /></td>
							</tr>
							<tr>
								<td>First Name:</td>
								<td><input type="text" name="firstName" /></td>
							</tr>
							<tr>
								<td>Last Name:</td>
								<td><input type="text" name="lastName" /></td>
							</tr>
							<tr>
								<td>Password:</td>
								<td><input type="text" name="password" /></td>
							</tr>
							<tr>
								<td>Role:</td>
								<td><select name="role">
										<option value="bloodBankUser">Blood Bank User</option>
										<option value="callcenter">Call Center User</option>
										<option value="superuser">Super User</option>
								</select>
								<input type="checkbox" name="chk_group[]" value="value1" />Value 1<br />
								<input type="checkbox" name="chk_group[]" value="value2" />Value 2<br />
								<input type="checkbox" name="chk_group[]" value="value3" />Value 3<br />
								</td>
							</tr>
							<tr>
								<td><input type="reset" value="Reset" /></td>
								<td><input type="submit" value="Create" /></td>
							</tr>
						</table>
					</form>

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
