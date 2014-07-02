
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<%@ include file="/Head.jsp"%>
<link rel="stylesheet" href="/css/redmond/jquery-ui-1.10.3.custom.css" />
<script src="/js/jquery-1.10.2.js"></script>
<script src="/js/jquery-ui-1.10.3.custom.js"></script>
<script src="/js/yavni/MasterConfig.js" type="text/javascript"></script>
<script>
function initDropdowns() {
	//initCities('city', false);
	initStates('state', false);
	initCountries('country', false);
	selectDropdown('selectedGender', 'gender');
	selectDropdown('selectedBloodGroup', 'bloodGroup');
	//selectDropdown('selectedCity', 'city');
	selectDropdown('selectedState', 'state');
	selectDropdown('selectedCountry', 'country');
}
var cities;
$(function() {
	$.ajax({
		url : "/mvc/listCities",
		dataType : 'json',
		async: false,
		success : function(data) {
			cities = data;
			console.log(cities);
		}
	});
	$("#dob").datepicker({ dateFormat: 'dd/mm/yy' });
	$("#lastDonatedDate").datepicker({ dateFormat: 'dd/mm/yy' });
	$( "#city" ).autocomplete({
		source: cities,
	});
});

</script>
</head>

<body onload="initDropdowns()">
	<div class="wrapper">
		<div class="main_body">
			<%@ include file="../../../LeftPanel.jsp"%>

			<div class="center_bg1">
				<div class="lm_01">
					<div><b>Edit Donor: </b>${donor.firstName} ${donor.lastName}</div>
				</div>
				<div class="center_021">

					<form action="/mvc/su/saveDonor" method="post">
						<table width="100%" border="0" cellpadding="0" cellspacing="4">
							<tr>
								<td colspan="2"></td>
							</tr>
							<tr>
								<td width="169">First Name<span style="color: #F00;">*</span></td>
								<td>
									<input name="donorId" type="hidden" value="${donor.donorId}" />
									<input name="firstName" type="text" value="${donor.firstName}" />
								</td>
							</tr>
							<tr>
								<td width="169">Last Name<span style="color: #F00;">*</span></td>
								<td><input name="lastName" type="text" value="${donor.lastName}" size="34" /></td>
							</tr>
							<tr>
								<td>Email</td>
								<td><input name="email" type="text" value="${donor.address.email}" size="34" /></td>
							</tr>
							<tr>
								<td>Contact Numbers<strong> </strong><span style="color: #F00;">*</span></td>
								<td></td>
							</tr>
							<tr>
								<td>Residence : </td>
								<td><input type="text" name="offPhone" size="20" value="${donor.address.offPhone}" /></td>								
							</tr>
							<tr>
								<td>Office :</td>
								<td><input type="text" name="resiPhone" size="20" value="${donor.address.residencePhone}" /></td>
							</tr>
							<tr>
								<td>Mobile No.</td>
								<td><input type="text" name="mob" size="34" value="${donor.address.mobilePhone}" /></td>
							</tr>
							<tr>
								<td>Date of Birth<strong> </strong><span style="color: #F00;">*</span></td>								
								<td>
									<fmt:formatDate var='formattedDOB' value='${donor.dateOfBirth}' pattern='dd/MM/yyyy' />
									<input type="text" id="dob" name="dob" value="${formattedDOB}"/>
								</td>
							</tr>
							<tr>
								<td>Sex <span style="color: #F00;">*</span></td>
								<td>
									<input type="hidden" id="selectedGender" value="${donor.gender}" />
									<select id="gender" name="gender">
										<option value="">Select</option>
										<option value="M">Male</option>
										<option value="F">Female</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>Blood Group<strong> </strong><span style="color: #F00;">*</span></td>
								<td><label for="select"></label>
								<input type="hidden" id="selectedBloodGroup" value="${donor.bloodGroup}" /> 
								<select id="bloodGroup" name="bloodGroup" id="select">
										<option value="A+">A+</option>
										<option value="A-">A-</option>
										<option value="B+">B+</option>
										<option value="B-">B-</option>
										<option value="AB+">AB+</option>
										<option value="AB-">AB-</option>
										<option value="O+">O+</option>
										<option value="O-">O-</option>
								</select></td>
							</tr>
							<tr>
								<td>Last Blood Donated on</td>
								<td>
									<fmt:formatDate var='formattedLastDate' value='${donor.lastDonationDate}' pattern='dd/MM/yyyy' />
									<input id="lastDonatedDate" name="lastDonatedDate" type="text" value="${formattedLastDate}" />
								</td>
							</tr>
							<!-- <tr>
								<td></td>
								<td><input name="dont" type="checkbox" value="ok" /> Donated long back</td>
							</tr> -->
							<tr>
								<td>Height</td>
								<td><label for="textfield"></label> <input type="text" name="height" id="height" value="${donor.height}" /></td>
							</tr>
							<tr>
								<td>Weight</td>
								<td><label for="textfield2"></label> <input type="text" name="weight" id="weight" value="${donor.weight}" /></td>
							</tr>
							<tr>
								<td>BP</td>
								<td><label for="textfield3"></label> <input type="text" name="bp" id="bp" value="${donor.bp}" /></td>
							</tr>
							<tr>
								<td>Residential Address<strong> </strong><span style="color: #F00;">*</span></td>
								<td><textarea name="addressLine1" cols="32" rows="2">${donor.address.addressLine1}</textarea></td>
							</tr>
							<tr>
								<td>City</td>
								<td>
									<input type="text" id="city" name="city" value="${donor.address.city}" />
								 </td>
							</tr>
							<tr>
								<td>State</td>
								<td>
								<input type="hidden" id="selectedState" value="${donor.address.state}" />
								<select id="state" name="state">
										<option value="0">Select</option>
										<option value="West Bengal">West Bengal</option>
								</select></td>
							</tr>
							<tr>
								<td>Country</td>
								<td>
								<input type="hidden" id="selectedCountry" value="${donor.address.country}" />
								<select id="country" name="country">
										<option value="0">Select</option>
										<option value="India" selected="selected">India</option>
								</select></td>
							</tr>
							<tr>
								<td>Pin</td>
								<td><input name="pin" type="text" onkeypress="chky()" value="${donor.address.pin}" maxlength="6" /></td>
							</tr>
							<tr>
								<td valign="top">Additional Comments <br /> (if any)
								</td>
								<td><textarea name="comments" rows="4" cols="33">${donor.comments}</textarea></td>
							</tr>
							<tr>
								<td height="25" colspan="2"></td>
							</tr>
							<tr>
								<td colspan="2"><p>The above details furnished are true and accurate. Blood donation is a
										medical procedure and I accept total responsibility for any risks associated with the donation of my blood. I'm willing to be a voluntary
										blood donor without any inducement. I further state that no remuneration shall be accepted by me for donating blood. I further state that I
										have no objection if you publish my name and other particulars as a blood donor in your above site.</p></td>
							</tr>
							<tr align="center">
								<td valign="top" colspan="2"><input type="submit" value="Update" name="update" onclick="return chkFrm();" /> <input type="reset"
									value="Reset" name="reset" /></td>
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
	</div>
</body>
<%@ include file="/Footer.jsp"%>
</html>
