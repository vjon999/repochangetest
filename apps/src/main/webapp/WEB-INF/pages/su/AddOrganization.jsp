<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/Head.jsp"%>
<script src="/js/jquery-1.10.2.js"></script>
<script src="/js/jquery-ui-1.10.3.custom.js"></script>
<script>
	$(function() {
		$("#datepicker").datepicker({ dateFormat: 'dd-mm-yy' });

	});

	function initDropdowns() {
		$.ajax({
			url : "/mvc/listBanks",
			dataType : 'json',
			success : function(bankNames) {
				var i;
				var bankNamesDD = document.getElementById("bankName");
				var html = '';
				for (i = 0; i < bankNames.length; i++) {
					html += '<option value='+bankNames[i][0]+'>'
							+ bankNames[i][1] + '</option>';
				}
				bankNamesDD.innerHTML = html;
			}
		});

		$.ajax({
			url : "/mvc/listCities",
			dataType : 'json',
			success : function(cities) {
				var i;
				var citiesDD = document.getElementById("city");
				var html = '';
				for (i = 0; i < cities.length; i++) {
					html += '<option value='+cities[i]+'>' + cities[i]
							+ '</option>';
				}
				citiesDD.innerHTML = html;
			}
		});

		$.ajax({
			url : "/mvc/listStates",
			dataType : 'json',
			success : function(state) {
				var i;
				var stateDD = document.getElementById("state");
				var html = '';
				for (i = 0; i < state.length; i++) {
					html += '<option value='+state[i]+'>' + state[i]
							+ '</option>';
				}
				stateDD.innerHTML = html;
			}
		});

		$.ajax({
			url : "/mvc/listCountries",
			dataType : 'json',
			success : function(countries) {
				var i;
				var countryDD = document.getElementById("country");
				var html = '';
				for (i = 0; i < countries.length; i++) {
					html += '<option value='+countries[i]+'>' + countries[i]
							+ '</option>';
				}
				countryDD.innerHTML = html;
			}
		});
	}
</script>

<link rel="stylesheet" href="/css/redmond/jquery-ui-1.10.3.custom.css" />
</head>

<body onload="initDropdowns();">
	<%@ include file="/LeftPanel.jsp"%>
	<div class="center_bg1">
		<div class="lm_01">
			<div id="style_00">Add Organization</div>
		</div>
		<div class="center_021">
			<form action="/mvc/su/saveOrganization" method="post">
				<table width="75%">
					<tr>
						<td>Organization<span style="color: #F00;">*</span></td>
						<td><input name="organization" type="text" size="34" /></td>
					</tr>
					<tr>
						<td align="left" valign="top">Date</td>
						<td><input type="text" id="datepicker" name="date" /></td>
					</tr>
					<tr>
						<td>Area</td>
						<td><input name="area" type="text" /></td>
					</tr>
					<tr>
						<td>Blood Bank<strong> </strong><span style="color: #F00;">*</span></td>
						<td><select id="bankName" name="bankId">
						</select></td>
					</tr>
					<tr>
						<td>Sectary Name :</td>
						<td><input name="secretaryName" type="text" /></td>
					</tr>
					<tr>
						<td>Prisident Name.</td>
						<td><input name="presidentName" type="text" /></td>
					</tr>
					<tr>
						<td>Concerned Person Name</td>
						<td><input name="contactPerson" type="text" /></td>
					</tr>
					<tr>
						<td>Address<strong> </strong></td>
						<td><textarea name="addressLine1" cols="32" rows="2"></textarea></td>
					</tr>
					<tr>
						<td>Country</td>
						<td><select id="country" name="country">
						</select></td>
					</tr>
					<tr>
						<td>State</td>
						<td><select id="state" name="state">
						</select></td>
					</tr>
					<tr>
						<td>City</td>
						<td><select id="city" name="city">
						</select> or <input type="text" name="city_oth" size="12" value="" /></td>
					</tr>
					<tr>
						<td>Pin</td>
						<td><input name="pin" type="text" size="6" maxlength="6" /></td>
					</tr>
					<tr>
						<td valign="top">Additional Comments <br /> (if any)
						</td>
						<td><textarea name="comments" rows="4" cols="33"></textarea></td>
					</tr>
					<tr>
						<td colspan="2">
							<p>The above details furnished are true and accurate. Blood donation is a medical procedure and I accept total responsibility for any risks
								associated with the donation of my blood. I'm willing to be a voluntary blood donor without any inducement. I further state that no
								remuneration shall be accepted by me for donating blood. I further state that I have no objection if you publish my name and other particulars
								as a blood donor in your above site.</p>
						</td>
					</tr>
					<tr align="center">
						<td valign="top" colspan="2"><input type="submit" value="Submit" name="save" onclick="return chkFrm();" /> <input type="reset"
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
</body>
<%@ include file="/Footer.jsp"%>
</html>
