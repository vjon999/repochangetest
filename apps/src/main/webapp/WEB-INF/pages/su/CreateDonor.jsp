
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/Head.jsp"%>
</head>

<body>
	<div class="wrapper">
		<div class="main_body">
			<%@ include file="../../../LeftPanel.jsp"%>

			<div class="center_bg1">
				<div class="lm_01">
					<div id="style_00">Add Donor</div>
				</div>
				<div class="center_021">

					<form action="/mvc/su/saveDonor" method="post">
						<table width="100%" border="0" cellpadding="0" cellspacing="4">
							<tr>
								<td colspan="2" align="left" valign="top"></td>
							</tr>
							<tr>
								<td width="169" align="left" valign="top">First Name<span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><input name="firstName" type="text" value="" size="34" /></td>
							</tr>
							<tr>
								<td width="169" align="left" valign="top">Last Name<span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><input name="lastName" type="text" value="" size="34" /></td>
							</tr>
							<tr>
								<td align="left" valign="top">Email</td>
								<td align="left" valign="top"><input name="email" type="text" value="" size="34" /></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td height="25" colspan="2"></td>
							</tr>
							<tr>
								<td>Phone no<strong> </strong><span style="color: #F00;">*</span></td>
								<td>
									<!-- <input name="mob" type="text" value="" size="34" /> -->
								</td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td bgcolor="f5f5f5">Residence : <input type="text" name="offPhone" size="20" /></td>
								<td bgcolor="f5f5f5">Office :<br /> <input type="text" name="resiPhone" size="20" /></td>
							</tr>
							<tr>
								<td>Mobile No.</td>
								<td><input type="text" name="mob" size="34" /></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td colspan="2" align="left" valign="top"></td>
							</tr>
							<tr>
								<td align="left" valign="top" bgcolor="f5f5f5">Date of Birth<strong> </strong><span style="color: #F00;">*</span></td>
								<td align="left" valign="top" bgcolor="f5f5f5"><select name="day">

										<option value="00">Date..</option>

										<option value="1">1</option>

										<option value="2">2</option>

										<option value="3">3</option>

										<option value="4">4</option>

										<option value="5">5</option>

										<option value="6">6</option>

										<option value="7">7</option>

										<option value="8">8</option>

										<option value="9">9</option>

										<option value="10">10</option>

										<option value="11">11</option>

										<option value="12">12</option>

										<option value="13">13</option>

										<option value="14">14</option>

										<option value="15">15</option>

										<option value="16">16</option>

										<option value="17">17</option>

										<option value="18">18</option>

										<option value="19">19</option>

										<option value="20">20</option>

										<option value="21">21</option>

										<option value="22">22</option>

										<option value="23">23</option>

										<option value="24">24</option>

										<option value="25">25</option>

										<option value="26">26</option>

										<option value="27">27</option>

										<option value="28">28</option>

										<option value="29">29</option>

										<option value="30">30</option>

										<option value="31">31</option>

								</select> - <select name="month">

										<option value="00">Month..</option>

										<option value="01">Jan</option>

										<option value="02">Feb</option>

										<option value="03">Mar</option>

										<option value="04">Apr</option>

										<option value="05">May</option>

										<option value="06">Jun</option>

										<option value="07">Jul</option>

										<option value="08">Aug</option>

										<option value="09">Sep</option>

										<option value="10">Oct</option>

										<option value="11">Nov</option>

										<option value="12">Dec</option>

								</select> - <select name="year">

										<option value="0000">Year..</option>

										<option value="1945">1945</option>

										<option value="1946">1946</option>

										<option value="1947">1947</option>

										<option value="1948">1948</option>

										<option value="1949">1949</option>

										<option value="1950">1950</option>

										<option value="1951">1951</option>

										<option value="1952">1952</option>

										<option value="1953">1953</option>

										<option value="1954">1954</option>

										<option value="1955">1955</option>

										<option value="1956">1956</option>

										<option value="1957">1957</option>

										<option value="1958">1958</option>

										<option value="1959">1959</option>

										<option value="1960">1960</option>

										<option value="1961">1961</option>

										<option value="1962">1962</option>

										<option value="1963">1963</option>

										<option value="1964">1964</option>

										<option value="1965">1965</option>

										<option value="1966">1966</option>

										<option value="1967">1967</option>

										<option value="1968">1968</option>

										<option value="1969">1969</option>

										<option value="1970">1970</option>

										<option value="1971">1971</option>

										<option value="1972">1972</option>

										<option value="1973">1973</option>

										<option value="1974">1974</option>

										<option value="1975">1975</option>

										<option value="1976">1976</option>

										<option value="1977">1977</option>

										<option value="1978">1978</option>

										<option value="1979">1979</option>

										<option value="1980">1980</option>

										<option value="1981">1981</option>

										<option value="1982">1982</option>

										<option value="1983">1983</option>

										<option value="1984">1984</option>

										<option value="1985">1985</option>

										<option value="1986">1986</option>

										<option value="1987">1987</option>

										<option value="1988">1988</option>

										<option value="1989">1989</option>

										<option value="1990">1990</option>

										<option value="1991">1991</option>

										<option value="1992">1992</option>

										<option value="1993">1993</option>

										<option value="1994">1994</option>

										<option value="1995">1995</option>

										<option value="1996">1996</option>

										<option value="1997">1997</option>

										<option value="1998">1998</option>

										<option value="1999">1999</option>

										<option value="2000">2000</option>

										<option value="2001">2001</option>

										<option value="2002">2002</option>

										<option value="2003">2003</option>

										<option value="2004">2004</option>

										<option value="2005">2005</option>

										<option value="2006">2006</option>

										<option value="2007">2007</option>

										<option value="2008">2008</option>

										<option value="2009">2009</option>

										<option value="2010">2010</option>

										<option value="2011">2011</option>

										<option value="2012">2012</option>

										<option value="2013">2013</option>

										<option value="2014">2014</option>

										<option value="2015">2015</option>

										<option value="2016">2016</option>

										<option value="2017">2017</option>

										<option value="2018">2018</option>

										<option value="2019">2019</option>

										<option value="2020">2020</option>

										<option value="2021">2021</option>

										<option value="2022">2022</option>

										<option value="2023">2023</option>

										<option value="2024">2024</option>

										<option value="2025">2025</option>

								</select></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td align="left" valign="middle">Sex <span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><select name="gender">

										<option value="">Select</option>

										<option value="M">Male</option>

										<option value="F">Female</option>

								</select></td>
							</tr>
							<tr>
								<td colspan="2" align="left" valign="top"></td>
							</tr>

							<tr bgcolor="#f5f5f5">
								<td align="left" valign="middle"><br /> Blood Group<strong> </strong><span style="color: #F00;">*</span></td>
								<td valign="top"><label for="select"></label> <select name="bloodGroup" id="select">
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
								<td align="left" valign="middle" bgcolor="f5f5f5">Last Blood Donated on</td>
								<td align="left" valign="top" bgcolor="f5f5f5"><select name="day1">

										<option value="00">Date..</option>

										<option value="1">1</option>

										<option value="2">2</option>

										<option value="3">3</option>

										<option value="4">4</option>

										<option value="5">5</option>

										<option value="6">6</option>

										<option value="7">7</option>

										<option value="8">8</option>

										<option value="9">9</option>

										<option value="10">10</option>

										<option value="11">11</option>

										<option value="12">12</option>

										<option value="13">13</option>

										<option value="14">14</option>

										<option value="15">15</option>

										<option value="16">16</option>

										<option value="17">17</option>

										<option value="18">18</option>

										<option value="19">19</option>

										<option value="20">20</option>

										<option value="21">21</option>

										<option value="22">22</option>

										<option value="23">23</option>

										<option value="24">24</option>

										<option value="25">25</option>

										<option value="26">26</option>

										<option value="27">27</option>

										<option value="28">28</option>

										<option value="29">29</option>

										<option value="30">30</option>

										<option value="31">31</option>

								</select> - <select name="month1">

										<option value="00">Month..</option>

										<option value="01">Jan</option>

										<option value="02">Feb</option>

										<option value="03">Mar</option>

										<option value="04">Apr</option>

										<option value="05">May</option>

										<option value="06">Jun</option>

										<option value="07">Jul</option>

										<option value="08">Aug</option>

										<option value="09">Sep</option>

										<option value="10">Oct</option>

										<option value="11">Nov</option>

										<option value="12">Dec</option>

								</select> - <select name="year1">

										<option value="0000">Year..</option>

										<option value="2010">2010</option>

										<option value="2011">2011</option>

										<option value="2012">2012</option>

										<option value="2013">2013</option>

								</select></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td align="left" valign="top"></td>
								<td align="left" valign="top"><input name="dont" type="checkbox" value="ok" /> Donated long back</td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td align="left" valign="top">Height</td>
								<td align="left" valign="top"><label for="textfield"></label> <input type="text" name="height" id="textfield" /></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td align="left" valign="top">Weight</td>
								<td align="left" valign="top"><label for="textfield2"></label> <input type="text" name="weight" id="textfield2" /></td>
							</tr>
							<tr>
								<td align="left" valign="top">BP</td>
								<td align="left" valign="top"><label for="textfield3"></label> <input type="text" name="bp" id="textfield3" /></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td align="left" valign="top">Residential Address<strong> </strong><span style="color: #F00;">*</span></td>
								<td align="left" valign="top"><textarea name="address" cols="32" rows="2"></textarea></td>
							</tr>
							<tr valign="middle">
								<td align="left">Country</td>
								<td align="left"><select name="country" size="1" onchange="show();">

										<option value="0">Select</option>

										<option value="Afghanistan">Afghanistan</option>

										<option value="Albania">Albania</option>

										<option value="Algeria">Algeria</option>

										<option value="American Samoa">American Samoa</option>

										<option value="Andorra">Andorra</option>

										<option value="Angola">Angola</option>

										<option value="Anguilla">Anguilla</option>

										<option value="Antarctica">Antarctica</option>

										<option value="Argentina">Argentina</option>

										<option value="Armenia">Armenia</option>

										<option value="Aruba">Aruba</option>

										<option value="Australia">Australia</option>

										<option value="Austria">Austria</option>

										<option value="Azerbaijan">Azerbaijan</option>

										<option value="Bahamas">Bahamas</option>

										<option value="Bahrain">Bahrain</option>

										<option value="Bangladesh">Bangladesh</option>

										<option value="Barbados">Barbados</option>

										<option value="Belarus">Belarus</option>

										<option value="Belgium">Belgium</option>

										<option value="Bhutan">Bhutan</option>

										<option value="Bolivia">Bolivia</option>

										<option value="Brazil">Brazil</option>

										<option value="Brunei">Brunei</option>

										<option value="Bulgaria">Bulgaria</option>

										<option value="Cameroon">Cameroon</option>

										<option value="Chile">Chile</option>

										<option value="China">China</option>

										<option value="Colombia">Colombia</option>

										<option value="Congo">Congo</option>

										<option value="Egypt">Egypt</option>

										<option value="France">France</option>

										<option value="Germany">Germany</option>

										<option value="Ghana">Ghana</option>

										<option value="Greece">Greece</option>

										<option value="Honduras">Honduras</option>

										<option value="Hungary">Hungary</option>

										<option value="Iceland">Iceland</option>

										<option value="India" selected="selected">India</option>

										<option value="Indonesia">Indonesia</option>

										<option value="Iran">Iran</option>

										<option value="Iraq">Iraq</option>

										<option value="Ireland">Ireland</option>

										<option value="Israel">Israel</option>

										<option value="Italy">Italy</option>

										<option value="Jamaica">Jamaica</option>

										<option value="Japan">Japan</option>

										<option value="Jordan">Jordan</option>

										<option value="Kazakhstan">Kazakhstan</option>

										<option value="Kenya">Kenya</option>

										<option value="Kiribati">Kiribati</option>

										<option value="South Korea">South Korea</option>

										<option value="North Korea">North Korea</option>

										<option value="Kuwait">Kuwait</option>

										<option value="Malaysia">Malaysia</option>

										<option value="Maldives">Maldives</option>

										<option value="Mali">Mali</option>

										<option value="Nepal">Nepal</option>

										<option value="Netherlands">Netherlands</option>

										<option value="New Caledonia">New Caledonia</option>

										<option value="New Zealand">New Zealand</option>

										<option value="Norway">Norway</option>

										<option value="Oman">Oman</option>

										<option value="Pakistan">Pakistan</option>

										<option value="Paraguay">Paraguay</option>

										<option value="Peru">Peru</option>

										<option value="Philippines">Philippines</option>

										<option value="Poland">Poland</option>

										<option value="Portugal">Portugal</option>

										<option value="Qatar">Qatar</option>

										<option value="Romania">Romania</option>

										<option value="Russia">Russia</option>

										<option value="Saudi Arabia">Saudi Arabia</option>

										<option value="Singapore">Singapore</option>

										<option value="South Africa">South Africa</option>

										<option value="Spain">Spain</option>

										<option value="Sri Lanka">Sri Lanka</option>

										<option value="Sudan">Sudan</option>

										<option value="Suriname">Suriname</option>

										<option value="Swaziland">Swaziland</option>

										<option value="Sweden">Sweden</option>

										<option value="Switzerland">Switzerland</option>

										<option value="Thailand">Thailand</option>

										<option value="Tunisia">Tunisia</option>

										<option value="Turkey">Turkey</option>

										<option value="Ukraine">Ukraine</option>

										<option value="United Arab Emirates">United Arab Emirates</option>

										<option value="United Kingdom">United Kingdom</option>

										<option value="United States">United States</option>

										<option value="Uruguay">Uruguay</option>

										<option value="Uzbekistan">Uzbekistan</option>

										<option value="Yemen">Yemen</option>

										<option value="Yugoslavia">Yugoslavia</option>

										<option value="Zambia">Zambia</option>

										<option value="Zimbabwe">Zimbabwe</option>
								</select></td>
							</tr bgcolor="#f5f5f5">
							<tr valign="middle" bgcolor="#f5f5f5">
								<td align="left" bgcolor="f5f5f5">State</td>
								<td align="left" bgcolor="f5f5f5"><select name="state" onchange="show();">

										<option value="0">Select</option>

										<option value="Andaman &amp; Nicobar">Andaman &amp; Nicobar</option>

										<option value="Andhra Pradesh">Andhra Pradesh</option>

										<option value="Arunachal Pradesh">Arunachal Pradesh</option>

										<option value="Assam">Assam</option>

										<option value="Bihar">Bihar</option>

										<option value="Chandigarh">Chandigarh</option>

										<option value="Chhattisgarh">Chhattisgarh</option>

										<option value="Dadra &amp; Nagar Haveli">Dadra &amp; Nagar Haveli</option>

										<option value="Daman &amp; Diu">Daman &amp; Diu</option>

										<option value="Delhi">Delhi</option>

										<option value="Goa">Goa</option>

										<option value="Gujarat">Gujarat</option>

										<option value="Haryana">Haryana</option>

										<option value="Himachal Pradesh">Himachal Pradesh</option>

										<option value="J &amp; K">J &amp; K</option>

										<option value="Jharkhand">Jharkhand</option>

										<option value="Karnataka">Karnataka</option>

										<option value="Kerala">Kerala</option>

										<option value="Lakshadweep">Lakshadweep</option>

										<option value="Madhya Pradesh">Madhya Pradesh</option>

										<option value="Maharastra">Maharastra</option>

										<option value="Manipur">Manipur</option>

										<option value="Meghalaya">Meghalaya</option>

										<option value="Mizoram">Mizoram</option>

										<option value="Nagaland">Nagaland</option>

										<option value="Orissa">Orissa</option>

										<option value="Pondicherry">Pondicherry</option>

										<option value="Punjab">Punjab</option>

										<option value="Rajasthan">Rajasthan</option>

										<option value="Sikkim">Sikkim</option>

										<option value="Tamil Nadu">Tamil Nadu</option>

										<option value="Tripura">Tripura</option>

										<option value="Uttar Pradesh">Uttar Pradesh</option>

										<option value="Uttaranchal">Uttaranchal</option>

										<option value="West Bengal">West Bengal</option>

								</select></td>
							</tr>
							<tr valign="middle">
								<td align="left">City</td>
								<td align="left"><select name="city">

										<option value="0">Select</option>
										<option value="Kolkata">Kolkata</option>
										<option value="Durgapur">Durgapur</option>
										<option value="Asansol">Asansol</option>
										<option value="Bardhaman">Bardhaman</option>
										<option value="Outside India">Outside India</option>

								</select> or <input type="text" name="resi_city_oth" size="12" value="" /></td>
							</tr>
							<tr valign="middle" bgcolor="#f5f5f5">
								<td align="left" bgcolor="f5f5f5">Pin</td>
								<td align="left" bgcolor="f5f5f5"><input name="pin" type="text" onkeypress="chky()" value="" size="12" maxlength="6" /></td>
							</tr>
							<tr>
								<td colspan="2" align="left" valign="top"></td>
							</tr>
							<tr>
								<td colspan="2" valign="top"></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td valign="top">Additional Comments <br /> (if any)
								</td>
								<td><textarea name="comments" rows="4" cols="33"></textarea></td>
							</tr>
							<tr>
								<td height="25" colspan="2" align="left" valign="top"></td>
							</tr>
							<tr bgcolor="#f5f5f5">
								<td align="left" valign="top" colspan="2"><p align="justify">The above details furnished are true and accurate. Blood donation is a
										medical procedure and I accept total responsibility for any risks associated with the donation of my blood. I'm willing to be a voluntary
										blood donor without any inducement. I further state that no remuneration shall be accepted by me for donating blood. I further state that I
										have no objection if you publish my name and other particulars as a blood donor in your above site.</p></td>
							</tr>
							<tr>
								<td align="left" valign="top" colspan="2"></td>
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
		</div>		
	</div>
</body>
<%@ include file="/Footer.jsp"%>
</html>
