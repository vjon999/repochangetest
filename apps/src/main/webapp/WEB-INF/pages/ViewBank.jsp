
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="../../Head.jsp" %>
</head>
<body>

	<table id="mainTable" style="border: thin; border-color: black;">
		<tbody>
			<tr>
				<td>
					<jsp:include page="/LeftPanel.jsp" />
				</td>
				
				<td>
					<div class="wrapper">		
						<div class="main_body">			
							<div class="center_bg1">
								<div class="lm_01">
									<div id="style_00">Blood Banks Information</div>
								</div>
								<div class="center_021">
									
										<table width="100%" border="0" cellpadding="0" cellspacing="4">
											<tr>
												<td colspan="2" align="left" valign="top"></td>
											</tr>
											<tr>
												<td width="169" align="left" valign="top">Name<span style="color: #F00;">*</span></td>
												<td align="left" valign="top">${bloodBank.name}</td>
											</tr>
											<tr bgcolor="#f5f5f5">
												<td align="left" valign="top" bgcolor="f5f5f5">Address</td>
												<td align="left" valign="top" bgcolor="f5f5f5">${bloodBank.address.addressLine1}</td>
											</tr>
											<tr>
												<td align="left" valign="top">District <span style="color: #F00;">*</span>
												</td>
												<td align="left" valign="top">${bloodBank.address.district}</td>
											</tr>
											<tr bgcolor="#f5f5f5">
												<td height="25">State</td>
												<td height="25">${bloodBank.address.state}</td>
											</tr>
											<tr>
												<td colspan="1">Phone no<strong> </strong><span style="color: #F00;">*</span></td>
												<td align="left" valign="top">${bloodBank.address.offPhone}</td>
											</tr>
											<tr>
												<td colspan="1">Email<strong> </strong><span style="color: #F00;">*</span></td>
												<td align="left" valign="top">${bloodBank.address.email}</td>
											</tr>
											<tr>
												<td colspan="1">CMO Name<strong> </strong><span style="color: #F00;">*</span></td>
												<td align="left" valign="top">${bloodBank.cmo}</td>
											</tr>
																		
										</table>
				
									<br />
									<div id="style_011">
										<p>&nbsp;</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</td>
				
				<td></td>
			</tr>
		</tbody>
	</table>
</body>
</html>
