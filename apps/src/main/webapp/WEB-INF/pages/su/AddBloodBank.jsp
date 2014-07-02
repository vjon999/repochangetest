
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
					<div id="style_00">Add Blood Banks</div>
				</div>
				<div class="center_021">
					<form action="/mvc/su/saveBank" method="post">
						<table width="100%" border="0" cellpadding="0" cellspacing="4">
							<tr>
								<td colspan="2" align="left" valign="top"></td>
							</tr>
							<tr>
								<td width="169" align="left" valign="top">Name<span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><input name="bankName" type="text" value="" size="34" /></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td align="left" valign="top" bgcolor="f5f5f5">Address <span style="color: #F00;">*</span></td>
								<td align="left" valign="top" bgcolor="f5f5f5"><input name="addressLine1" type="text" value="" size="34" /></td>
							</tr>
							<tr>
								<td align="left" valign="top">District <span style="color: #F00;">*</span>
								</td>
								<td align="left" valign="top"><label for="select"></label> <select name="district" id="state">
										<option value="">Select District....</option>
										<option value="Kolkata">Kolkata</option>
										<option value="Howrah">Howrah</option>

								</select></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td height="25">State</td>
								<td height="25"><select name="state" id="state">
										<option value="">Select District....</option>
										<option value="Delhi">Delhi</option>
										<option value="West Bengal">West Bengal</option>
								</select></td>
							</tr>
							<tr>
								<td>Phone no<strong> </strong><span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><input name="offPhone" type="text" value="" size="34" /></td>
							</tr>
							<tr>
								<td>email ID:<strong> </strong><span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><input name="email" type="text" value="" size="34" /></td>
							</tr>
							<tr>
								<td>CMO Name<strong> </strong><span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><input name="cmo" type="text" value="" size="34" /></td>
							</tr>
							<tr>
								<td align="left" valign="top" colspan="2"></td>
							</tr>
							<tr align="center">
								<td valign="top" colspan="2"><input type="submit" value="Submit" name="save" onclick="return chkFrm();" /> 
								<input type="reset" value="Reset" name="reset" /></td>
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

		<div class="footer">
			<div class="footer_03">
				<div id="style_01_1">
					All Rights Reserved | Best Viewed at 1024 X 768 | e-Branding by <a href="http://websoft-technologies.com/" class="ho_02" target="_blank">
						Websoft Technologies</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
