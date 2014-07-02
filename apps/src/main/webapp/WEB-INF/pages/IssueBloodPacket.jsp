<script src="/js/yavni/IssuePacket.js"></script>
<form id="removePacketForm" method="post" action="/mvc/bank/issueBloodPacket">
	<table id="packetInfo">
		<tr>
			<td>Blood Group:</td>
			<td><select id="bloodGroup" name="bloodGroup" onchange="enableBloodType();">
					<option value="Select">Select Blood Group</option>
					<option value="A+">A+</option>
					<option value="A-">A-</option>
					<option value="B+">B+</option>
					<option value="B-">B-</option>
					<option value="AB+">AB+</option>
					<option value="AB-">AB-</option>
					<option value="O-">O+</option>
					<option value="O-">O-</option>
			</select></td>
			<td>Blood Type:</td>
			<td><select id="bloodType" name="bloodType" onChange="fetchBloodPacket();" disabled="disabled">
					<option value="Select">Select Blood Type</option>
					<option value="PLASMA">Plasma</option>
					<option value="RBC">RBC</option>
					<option value="WBC">WBC</option>
					<option value="PLATELETS">Platelets</option>
			</select></td>
		</tr>
		<tr>
			<td>Bank Name</td>
			<td><input id="bankName" type="text" name="bankName" readonly="readonly" /></td>
			<td>Batch</td>
			<td>
				<input id="batch" type="text" name="batch" readonly="readonly" />
				<input id="id" type="hidden" name="id" readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td>Received Date</td>
			<td><input id="receivedDate" type="text" name="receivedDate" readonly="readonly" /></td>
		</tr>

	</table>
	<hr class="style-four">
	<table id="patientInfo">
		<tr>
			<td>Patient Name</td>
			<td><input id="patientName" name="patientName" /></td>
			<td>Doctor</td>
			<td><input id="doctorName" name="doctorName" /></td>
		</tr>
		<tr>
			<td>Hospital</td>
			<td><input id="hospitalName" name="hospitalName" /></td>
			<td>Bed No</td>
			<td><input id="bedNumber" name="bedNumber" /></td>
		</tr>
		<tr>
			<td>Remark</td>
			<td><input id="remarks" name="remarks" /></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td colspan="2"><input type="button" value="Issue" onclick="submitIssuePacketForm('removePacketForm')"> 
			<input type="reset" value="Reset" onclick="this.form.reset();" ></td>
			<td></td>
		</tr>
	</table>
</form>
