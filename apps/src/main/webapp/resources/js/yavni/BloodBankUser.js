function initDataTables() {
	$('#bloodBalance')
	.dataTable(
			{
				"bJQueryUI" : true,
				"bProcessing" : true,
				"bServerSide": false,
				"bAutoWidth": false,
				"sScrollX": "50%", 
				"sScrollXInner": "100%",
				"width" : "10px",
				"sAjaxSource" : "/mvc/bank/inventory?bankId="+bankID,
				"aoColumns" : [ {
					"mData" : "bloodGroup", "sWidth": "10%",
				}, {
					"mData" : "bloodType", "sWidth": "20%",
				}, {
					"mData" : "count", "sWidth": "10%", 
				}, {
					"mData" : "recent", "sWidth": "30%",
				}, {
					"mData" : "oldest", "sWidth": "30%",
				}, {
					"mData" : "bloodGroup", "mRender": function ( data, type, full ) {
		                  return '<button class="issuePacket" style="width: 90%;" onclick="issuePacket(\''+data+'\', this)">issue</button>';
					}, "sWidth": "20%",
				} ],
			}).columnFilter();

$('#verifiableRecords')
	.dataTable(
			{
				"bJQueryUI" : true,
				"bProcessing" : true,
				"bServerSide": false,
				"width" : "10px",
				"sAjaxSource" : "/mvc/bank/loadVerifiable?bankId="+bankID,
				"aoColumns" : [ {
					"mData" : "id"
				}, {
					"mData" : "batch"
				}, {
					"mData" : "bloodGroup"
				}, {
					"mData" : "bloodType"
				}, {
					"mData" : "receivedDate"
				}, {
					"mData" : "issuedDate"
				}, {
					"mData" : "id", "mRender": function ( data, type, full ) {
		                  return '<input type=\'checkbox\' id=\''+data+'Verify\' name=\''+data+'\' onclick=\'verifyItem('+data+')\'></input>';
					}
				}, {
					"mData" : "id", "mRender": function ( data, type, full ) {
		                  return '<input type=\'checkbox\' id=\''+data+'Fail\' name=\''+data+'\' onclick=\'verificationFail('+data+')\'></input>';
					}
				}, {
					"mData" : "id", "mRender": function ( data, type, full ) {
		                  return '<textarea type=\'checkbox\' id=\''+data+'Remarks\' name=\'verifierRemarks\' ></textarea>';
					}
				},
				],
			});

$('#bloodRecords')
	.dataTable(
			{
				"bJQueryUI" : true,
				"bProcessing" : true,
				"bServerSide": false,
				"width" : "10px",
				"sAjaxSource" : "/mvc/bank/allInventories?bankId="+bankID,
				"aoColumns" : [ {
					"mData" : "id"
				}, {
					"mData" : "batch"
				}, {
					"mData" : "bloodGroup"
				}, {
					"mData" : "bloodType"
				}, {
					"mData" : "receivedDate"
				}, {
					"mData" : "issuedDate"
				}, {
					"mData" : "verified"
				}, {
					"mData" : "verifiedBy"
				}, {
					"mData" : "verifiedDate"
				}, {
					"mData" : "approved"
				}, {
					"mData" : "approvedBy"
				}, {
					"mData" : "approvedDate"
				} ],
			});

$('#approvableRecords')
.dataTable(
		{
			"bJQueryUI" : true,
			"bProcessing" : true,
			"bServerSide": false,
			"width" : "10px",
			"sAjaxSource" : "/mvc/bank/loadApprovable?bankId="+bankID,
			"aoColumns" : [ {
				"mData" : "id"
			}, {
				"mData" : "batch"
			}, {
				"mData" : "bloodGroup"
			}, {
				"mData" : "bloodType"
			}, {
				"mData" : "receivedDate"
			}, {
				"mData" : "issuedDate"
			}, {
				"mData" : "verified"
			}, {
				"mData" : "verifiedBy"
			}, {
				"mData" : "verifiedDate"
			}, {
				"mData" : "id", "mRender": function ( data, type, full ) {
	                  return '<input type=\'checkbox\' id=\''+data+'Approve\' name=\''+data+'\' onclick=\'approveItem('+data+')\'></input>';
				}
			}, {
				"mData" : "id", "mRender": function ( data, type, full ) {
	                  return '<input type=\'checkbox\' id=\''+data+'Reject\' name=\''+data+'\' onclick=\'rejectItem('+data+')\'></input>';
				}
			}, {
				"mData" : "id", "mRender": function ( data, type, full ) {
	                  return '<textarea type=\'checkbox\' id=\''+data+'Remarks\' name=\'approverRemarks\' ></textarea>';
				}
			}],
		});
}

function cleanPacketForm() {
	var field1 = document.getElementById('batch');
	var field2 = document.getElementById('rebatch');	
	var fieldBatchError = document.getElementById("batchError");
	fieldBatchError.innerHTML = '';
	field1.value = '';
	field2.value = '';
	field1.className = "";
	field2.className = "";
	$("#dialog").dialog('option','width',DEF_PACKET_WIDTH);
}

function compareFields(id1, id2) {	
	var field1 = document.getElementById(id1);
	var field2 = document.getElementById(id2);	
	var fieldBatchError = document.getElementById("batchError");
	
	if (field1.value != field2.value) {
		// alert('fields' + id1 + ' and ' + id2+' does not match');
		field1.className = "ui-state-error";
		field2.className = "ui-state-error";
	} else {
		field1.className = "";
		field2.className = "";
		fieldBatchError.innerHTML = '';
	}
	
	$.ajax({
		url:"/mvc/search/isUniquePacket?batch="+field1.value,
		dataType: 'json',
		success:function(bloodPacket) {
			if(bloodPacket ==  false) {
				field1.className = "ui-state-error";
				field2.className = "ui-state-error";
				fieldBatchError.innerHTML='already present';
				$("#dialog").dialog('option','width',DEF_PACKET_WIDTH+100);
			}
		}
	});
}

function showForm(id) {
	var div = document.getElementById(id);
	div.style.visible;
}

function submitForm(id) {
	if(!validateForm()) {
		alert('validation failed');
		return;
	}
	var form = document.getElementById(id);
	var action = form.action;
	$.ajax({
        url     : action,
        type    : 'POST',
        data    : $( "#"+id ).serializeArray(),
        success : function(resp) {
        	$("#verifiableRecords").dataTable().fnReloadAjax();
        	//console.log("Submitted !!!");
            //$("#bloodBalance").dataTable().fnDraw();
           /* refreshTable('#approvableRecords', '/mvc/bank/loadApprovable?bankId='+bankID);
            refreshTable('#verifiableRecords', '/mvc/bank/loadVerifiable?bankId='+bankID);
            refreshTable('#bloodRecords', '/mvc/bank/allInventories?bankId='+bankID);
            refreshTable('#bloodBalance', '/mvc/bank/inventory?bankId='+bankID);*/
            $("#addBloodPacketDialog").dialog("close");
        },
        error   : function(resp){
            //alert(JSON.stringify(resp));
        }
     });
}

function validateForm() {  
	var batch = document.getElementById('batch').value;
	var rebatch = document.getElementById('rebatch').value;
	var bloodGroup = document.getElementById('bloodGroup').value;
	var receivedDate = document.getElementById('datepicker').value;
	//console.log(batch+bloodGroup+"receivedDate: "+receivedDate+' '+(typeof receivedDate != 'undefined'));
	var valid = true;
	valid = valid && (batch != null) && (batch !== null);
	valid = valid && (rebatch != null) && (rebatch != null);
	valid = valid && (bloodGroup != null) && (bloodGroup != null);
	valid = valid && (receivedDate != '') && (typeof receivedDate != 'undefined');
	valid = valid && (batch == rebatch);
	return valid;
}

function fetchBloodPacket() {
	var data = $("#removePacketForm" ).find('#bloodGroup, #bloodType').serialize();
	$.ajax({
        url     : '/mvc/bank/fetchBlooadPacket',
        dataType: 'json',
        type    : 'GET',
        data	: data,
        success : function(bloodStock) {
        	$("#removePacketForm" ).find('#bankName').val(bloodStock.bankName);
        	$("#removePacketForm" ).find('#batch').val(bloodStock.batch);
        	$("#removePacketForm" ).find('#id').val(bloodStock.id);
        	$("#removePacketForm" ).find('#receivedDate').val(bloodStock.receivedDate);
        },
     });
}

function enableBloodType() {
	$("#removePacketForm" ).find('#bloodType').attr('disabled', false);
}

