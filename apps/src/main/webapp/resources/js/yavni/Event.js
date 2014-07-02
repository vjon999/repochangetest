function loadDonorForm(id) {
	$('#'+id).load('/mvc/event/donorForm');
}

function loadOrganizationCodes(orgCodes) {
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
}

function saveDonor(form) {
	var action = form.action;
	donor = new Object();
	donor.firstName = $('#donorForm').find('#firstName').val();
	donor.lastName = $('#donorForm').find('#lastName').val();
	donor.gender = $('#donorForm').find('#gender').val();
	donor.dateOfBirth = $('#donorForm').find('#dateOfBirth').val();
	donor.bloodGroup = $('#donorForm').find('#bloodGroup').val();
	donor.lastDonationDate = $('#donorForm').find('#lastDonationDate').val();
	donor.height = $('#donorForm').find('#height').val();
	donor.weight = $('#donorForm').find('#weight').val();
	donor.bp = $('#donorForm').find('#bp').val();
	donor.comments = $('#donorForm').find('#comments').val();
	
	address = new Object();
	address.country = $('#donorForm').find('#country').val();
	address.state = $('#donorForm').find('#state').val();
	address.city = $('#donorForm').find('#city').val();
	address.pin = $('#donorForm').find('#pin').val();
	address.email = $('#donorForm').find('#email').val();
	address.residencePhone = $('#donorForm').find('#residencePhone').val();
	address.offPhone = $('#donorForm').find('#offPhone').val();
	address.mobilePhone = $('#donorForm').find('#mobilePhone').val();
	
	donor.address = address;
	console.log(JSON.stringify(donor));
	/*$.ajax({
		headers: { 
	        'Accept': 'application/resources/js/on',
	        'Content-Type': 'application/resources/js/on' 
	    },
        url     : action,
        dataType: 'json',
        type    : 'POST',
        data	: JSON.stringify(donor),
        success : function(wrapper) {
        	//$("#saveFormMsgDialog").dialog('open');
        	form.reset();
        	//$('#centerPanel').load('/mvc/event/viewEvent?eventId='+wrapper.msg);
        },
     });*/
}