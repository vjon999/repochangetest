<%-- <%@ include file="/Head.jsp"%> --%>
<script>
var banks;
var bankNames=new Array();
var orgs;
var orgCodes = new Array();
$(function() {	
	$.ajax({
		url : "/mvc/listBanks",
		dataType : 'json',
		async: false,
		success : function(data) {
			banks = data;
			//var curIndex = -1;
			for(var i=0;i<data.length;i++) {
				/* if(bankID = data[i][0]) {
					curIndex = i;
				} */
				bankNames[i] = data[i][1];
			}
			/* bankNames.splice(curIndex, 1);
			banks.splice(curIndex, 1); */
		}
	});
	$.ajax({
		url : "/mvc/listOrganizations",
		dataType : 'json',
		async: false,
		success : function(data) {
			orgs = data;
			for(var i=0;i<data.length;i++) {
				orgCodes[i] = data[i][2];
				console.log(data[i][2]);
			}
		}
	});
	$("#bloodBankName").autocomplete({source : bankNames,});
	$("#orgCode").autocomplete({source : orgCodes,});
	$("#activityDate").datepicker({dateFormat: 'dd/mm/yy',});
	$("#saveFormMsgDialog").dialog({position: 'relative', modal: true, autoOpen : false,});
});

function saveEvent(form) {
	var action = form.action;
	event = new Object();
	event.organizationCode = $('#updateActivityForm').find('#orgCode').val();
	event.organizationId = $('#updateActivityForm').find('#orgId').val();
	event.requirement = $('#updateActivityForm').find('#requirement').val();
	event.fulfilment = $('#updateActivityForm').find('#fulfilment').val();
	event.expectedDonorCount = $('#updateActivityForm').find('#expectedDonorCount').val();
	event.bloodBankId = $('#updateActivityForm').find('#bloodBankId').val();
	var activityDtStr = $('#updateActivityForm').find('#activityDate').val()
	event.activityDate = new Date(activityDtStr);
	
	address = new Object();
	address.area = $('#updateActivityForm').find('#area').val(); 
	event.activityLocation = address;
	console.log(JSON.stringify(event));
	$.ajax({
		headers: { 
	        'Accept': 'application/json',
	        'Content-Type': 'application/json' 
	    },
        url     : action,
        dataType: 'json',
        type    : 'POST',
        data	: JSON.stringify(event),
        success : function(wrapper) {
        	$("#saveFormMsgDialog").dialog('open');
        	form.reset();
        	$('#centerPanel').load('/mvc/event/viewEvent?eventId='+wrapper.msg);
        },
     });
}

function updateBankId(bankName) {console.log(bankName);
	for(var i=0;i<banks.length;i++) {
		if(bankName == banks[i][1]) {
			$('#bloodBankId').val(banks[i][0]);
			break;
		}
	}
}
function updateOrgId(orgCode) {
	console.log(orgCode);
	for(var i=0;i<orgs.length;i++) {
		if(orgCode == orgs[i][2]) {
			$('#orgId').val(orgs[i][0]);
			break;
		}
	}
}
</script>
<div class="lm_01" >
	<p><b>Create an Event</b></p>
	<form id='updateActivityForm' action='/mvc/event/saveEvent'>
		<table>
			<tr>
				<td>Organization Code:</td>
				<td><input type='text' id='orgCode' onblur="updateOrgId(this.value)"/><input type='hidden' id='orgId' /></td>
			</tr>
			<tr>
				<td>Activity Date:</td>
				<td><input type='text' id='activityDate' /></td>
			</tr>
			<tr>
				<td>Activity Location:</td>
				<td><input type='text' id='area' /></td>
			</tr>
			<tr>
				<td>Requirements</td>
				<td><textarea id='requirement'></textarea></td>
			</tr>
			<tr>
				<td>Fulfillments</td>
				<td><textarea id='fulfilment'></textarea></td>				
			</tr>
			<tr>
				<td>Expected Donor Count: </td>
				<td><input type='text' id='expectedDonorCount' /></td>
			</tr>
			<tr>
				<td>Blood Bank:</td>
				<td><input type='text' id='bloodBankName' onblur="updateBankId(this.value)"/> <input type='hidden' id='bloodBankId' /></td>
			</tr>
			<tr>
				<td><input type='reset' /></td>
				<td><button id='updateActivity' onclick="saveEvent(this.form)" type="button">Save</button></td>
			</tr>
		</table>
	</form>
</div>

<div id="saveFormMsgDialog" title="Save Event">
  <p>Event saved successfully.</p>
  <br/>
  <button onclick="$('#saveFormMsgDialog').dialog('close');">OK</button>
</div>