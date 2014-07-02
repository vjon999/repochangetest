function verify() {
	 var data=new Array();
	$('#verifiableRecords tr').filter(':has(:checkbox:checked)').each(function() {
		console.log("row");
		packet = new Object();
		packet.id=$(this).find('td:nth-child(1)').html();
		packet.batch=$(this).find('td:nth-child(2)').html();
		packet.bloodGroup=$(this).find('td:nth-child(3)').html();
		if($(this).find('td:nth-child(7)').children().prop('checked')) {
			packet.verified=true;
		}
		else if($(this).find('td:nth-child(8)').children().prop('checked')) {
			packet.verified=false;
		}
		packet.verifierRemark=$(this).find('td:nth-child(9)').children().val();
		data.push(packet);
	});
	console.log(JSON.stringify(data));
	$.ajax({
		headers: { 
	        'Accept': 'application/resources/js/on',
	        'Content-Type': 'application/resources/js/on' 
	    },
        url     : '/mvc/bank/verify',
        dataType: 'json',
        type    : 'POST',
        data	: JSON.stringify(data),
        success : function(bloodStock) {
        	$("#approvableRecords").dataTable().fnReloadAjax();
        	$("#verifiableRecords").dataTable().fnReloadAjax();
        },
     });
}

function verifyItem(id) {
	var reject = document.getElementById(id+'Fail');
	var approve = document.getElementById(id+'Verify');
	
	if(approve.checked == false) {
		rejectItem(id);
	}
	else {
		if(reject.checked == true) {
			reject.checked = false;
		}
	}
}

function verificationFail(id) {
	var approve = document.getElementById(id+'Verify');
	if(approve.checked == true) {
		approve.checked = false;
	}
}

function approve() {
	 var data=new Array();
	$('#approvableRecords tr').filter(':has(:checkbox:checked)').each(function() {
		console.log("row");
		packet = new Object();
		packet.id=$(this).find('td:nth-child(1)').html();
		packet.batch=$(this).find('td:nth-child(2)').html();
		packet.bloodGroup=$(this).find('td:nth-child(3)').html();
		if($(this).find('td:nth-child(10)').children().prop('checked')) {
			packet.approved=true;
		}
		else if($(this).find('td:nth-child(11)').children().prop('checked')) {
			packet.approved=false;
		}
		packet.approverRemark=$(this).find('td:nth-child(12)').children().val();
		data.push(packet);
	});
	console.log(JSON.stringify(data));
	$.ajax({
		headers: { 
	        'Accept': 'application/resources/js/on',
	        'Content-Type': 'application/resources/js/on' 
	    },
       url     : '/mvc/bank/approve',
       dataType: 'json',
       type    : 'POST',
       data	: JSON.stringify(data),
       success : function(bloodStock) {
    	 $("#approvableRecords").dataTable().fnReloadAjax();
    	 $("#bloodBalance").dataTable().fnReloadAjax();
       },
    });
}

function approveItem(id) {
	var reject = document.getElementById(id+'Reject');
	var approve = document.getElementById(id+'Approve');
	
	if(approve.checked == false) {
		rejectItem(id);
	}
	else {
		if(reject.checked == true) {
			reject.checked = false;
		}
	}
}

function rejectItem(id) {
	var approve = document.getElementById(id+'Approve');
	if(approve.checked == true) {
		approve.checked = false;
	}
}