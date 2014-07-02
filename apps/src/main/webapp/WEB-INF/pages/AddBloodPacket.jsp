<%@ include file="/Imports.jsp"%>
<script>
$(function() {	   
  $('#timepicker').timepicker();
   $("#datepicker").datepicker({
   	dateFormat: 'dd/mm/yy',
   });
 });
 
</script>

	<form id="addPacketForm" method="post" action="/mvc/bank/addPacket">
		<table id="addPacketTable">
			<tr>
				<td>Batch:</td>
				<td><input id="batch" type="text" name=batch /></td>
				<td><p id="batchError" class="ui-state-error"></p></td>
			</tr>
			<tr>
				<td>Re-enter Batch:</td>
				<td><input id="rebatch" type="text" name=rebatch onblur="compareFields('batch','rebatch' )" /></td>
				<td></td>
			</tr>
			<tr>
				<td>Blood Group:</td>
				<td><select id="bloodGroup" name="bloodGroup">
						<option value="A+">A+</option>
						<option value="A-">A-</option>
						<option value="B+">B+</option>
						<option value="B-">B-</option>
						<option value="AB+">AB+</option>
						<option value="AB-">AB-</option>
						<option value="O+">O+</option>
						<option value="O-">O-</option>
				</select></td>
				<td></td>
			</tr>
			<tr>
				<td>Receive Date:</td>
				<td><input type="text" id="datepicker" name="receivedDate" /></td>
				<td></td>
			</tr>
			<tr>
				<td>Time:</td>
				<td><input type="text" id="timepicker" name="receivedTime"></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td>
					<button id="update" onclick="submitForm('addPacketForm')">Update</button>
					<input type="reset" value="Reset" onclick="this.form.reset();" />
				</td>
				<td></td>
			</tr>
		</table>
	</form>
<script>
document.getElementById("datepicker").setAttribute('value', $.datepicker.formatDate('dd/mm/yy', new Date()));
document.getElementById("timepicker").setAttribute('value', getCurrentTime());
</script>