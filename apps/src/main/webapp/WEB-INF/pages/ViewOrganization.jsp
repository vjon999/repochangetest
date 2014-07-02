<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/Head.jsp"%>
</head>

<body>
	<%@ include file="/LeftPanel.jsp"%>
	<div class="center_bg1">
		<div class="lm_01">
			<table class="yavniHeader" style="width: 75%;">
				<tr>
					<td>Organization Information</td>
					<td style="text-align: right;"><a href="/mvc/editOrganization?organizationName=${organization.organizationName}">Edit</a></td>
				</tr>				
			</table>
		</div>
		<div class="center_021">
				<table width="75%">
					<tr>
						<td>Organization<span style="color: #F00;">*</span></td>
						<td>${organization.organizationName}</td>
					</tr>
					<tr>
						<td>Date</td>
						<td>${organization.campDate}</td>
					</tr>
					<tr>
						<td>Area</td>
						<td>${organization.address.area}</td>
					</tr>
					<tr>
						<td>Blood Bank<strong> </strong><span style="color: #F00;">*</span></td>
						<td>${organization.bloodBank.name}</td>
					</tr>
					<tr>
						<td>Sectary Name :</td>
						<td>${organization.secretaryName}</td>
					</tr>
					<tr>
						<td>Prisident Name.</td>
						<td>${organization.presidentName}</td>
					</tr>
					<tr>
						<td>Concerned Person Name</td>
						<td>${organization.contactPerson}</td>
					</tr>
					<tr>
						<td>Address<strong> </strong></td>
						<td>${organization.address.addressLine1}</td>
					</tr>
					<tr>
						<td>Country</td>
						<td>${organization.address.country}</td>
					</tr>
					<tr>
						<td>State</td>
						<td>${organization.address.state}</td>
					</tr>
					<tr>
						<td>City</td>
						<td>${organization.address.city}</td>
					</tr>
					<tr>
						<td>Pin</td>
						<td>${organization.address.pin}</td>
					</tr>
					<tr>
						<td valign="top">Additional Comments <br /> (if any)</td>
						<td>${organization.comments}</td>
					</tr>
				</table>
		</div>
	</div>
</body>
<%@ include file="/Footer.jsp"%>
</html>
