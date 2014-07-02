function submitIssuePacketForm(id) {
	if(!validatetIssuePacketForm()) {
		alert('validation failed');
		return;
	}
	var form = document.getElementById(id);
	var action = form.action;
	bloodStock = new Object();
	bloodStock.id=$('#removePacketForm').find('#id').val();
	bloodStock.batch = $('#removePacketForm').find('#batch').val();
	bloodStock.bloodGroup = $('#removePacketForm').find('#bloodGroup').val();
	bloodStock.bloodType = $('#removePacketForm').find('#bloodType').val();
	bloodStock.patientName = $('#removePacketForm').find('#patientName').val();
	bloodStock.doctorName = $('#removePacketForm').find('#doctorName').val();
	bloodStock.hospitalName = $('#removePacketForm').find('#hospitalName').val();
	bloodStock.bedNumber = $('#removePacketForm').find('#bedNumber').val();
	bloodStock.remarks = $('#removePacketForm').find('#remarks').val();
	bloodStock.bloodBankName = $('#removePacketForm').find('#bankName').val();
	bloodStock.bloodBankId = bankID;
	console.log(JSON.stringify(bloodStock));
	$.ajax({
		headers: { 
	        'Accept': 'application/resources/js/on',
	        'Content-Type': 'application/resources/js/on' 
	    },
        url     : action,
        dataType: 'json',
        type    : 'POST',
        data	: JSON.stringify(bloodStock),
        success : function(bloodStock) {
        	$("#bloodBalance").dataTable().fnReloadAjax();
        	 $("#issuePacketDialog").dialog("close");
        },
     });
}

function validatetIssuePacketForm() {
	return true;
}