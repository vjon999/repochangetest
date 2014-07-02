<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<%@ include file="/Head.jsp"%>
<style>
table {
	width: 75%;
}
</style>
</head>

<body>
	<div class="wrapper">
		<div class="main_body">
			<%@ include file="../../../LeftPanel.jsp"%>

			<div class="center_bg1">
				<table class="yavniHeader">
					<tr>
						<td>Donor Information</td>
						<td style="text-align: right;"><a href="/mvc/editDonor?donorId=${donor.donorId}">Edit</a></td>
					</tr>
				</table>
				<div class="center_021">
					<table width="100%" border="0" cellpadding="0" cellspacing="4">
						<tr>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td width="169">First Name<span style="color: #F00;">*</span></td>
							<td>${donor.firstName}</td>
						</tr>
						<tr>
							<td width="169">Last Name<span style="color: #F00;">*</span></td>
							<td>${donor.lastName}</td>
						</tr>
						<tr>
							<td>Email</td>
							<td>${donor.address.email}</td>
						</tr>
						<tr><td height="10px;"></td><td></td></tr>
						<tr>
							<td>Contact Numbers<strong> </strong></td>
							<td></td>
						</tr>
						<tr>
							<td>Residence : </td>
							<td>${donor.address.residencePhone}</td>
						</tr>
						<tr>
							<td>Office : </td>
							<td>${donor.address.offPhone}</td>
						</tr>
						<tr>
							<td>Mobile No.</td>
							<td>${donor.address.mobilePhone}</td>
						</tr>
						<tr><td height="10px;"></td><td></td></tr>
						<tr>
							<td>Date of Birth<strong> </strong><span style="color: #F00;">*</span></td>
							<td><fmt:formatDate value="${donor.dateOfBirth}" pattern="dd/MM/yyyy" /></td>
						</tr>
						<tr>
							<td>Sex <span style="color: #F00;">*</span></td>
							<td>${donor.gender}</td>
						</tr>
						<tr>
							<td>Blood Group<strong> </strong><span style="color: #F00;">*</span></td>
							<td>${donor.bloodGroup}</td>
						</tr>
						<tr>
							<td>Last Blood Donated on</td>
							<td><fmt:formatDate value="${donor.lastDonationDate}" pattern="dd/MM/yyyy" /></td>
						</tr>
<!-- 						<tr>
							<td></td>
							<td><input name="dont" type="checkbox" value="ok" /> Donated long back</td>
						</tr> -->
						<tr>
							<td>Height</td>
							<td>${donor.height}</td>
						</tr>
						<tr>
							<td>Weight</td>
							<td>${donor.weight}</td>
						</tr>
						<tr>
							<td>BP</td>
							<td>${donor.bp}</td>
						</tr>
						<tr>
							<td>Residential Address<strong> </strong><span style="color: #F00;">*</span></td>
							<td>${donor.address.addressLine1}</td>
						</tr>
						<tr>
							<td>Country</td>
							<td>${donor.address.country}</td>
						</tr>
						<tr>
							<td>State</td>
							<td>${donor.address.state}</td>
						</tr>
						<tr>
							<td>City</td>
							<td>${donor.address.city}</td>
						</tr>
						<tr>
							<td>Pin</td>
							<td>${donor.address.pin}</td>
						</tr>
						<tr>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td>Additional Comments <br /> (if any)
							</td>
							<td>${donor.comments}</td>
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
	
	<%@ include file="/Footer.jsp"%>
</body>

</html>
