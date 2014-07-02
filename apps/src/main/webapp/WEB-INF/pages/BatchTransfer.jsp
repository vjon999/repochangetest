<script>
	var banks = new Array();
	var batchDetails = new Array();
	var batches = new Array();
	$(function() {
		var bankNames=new Array();
		$.ajax({
			url : "/mvc/listBanks",
			dataType : 'json',
			async: false,
			success : function(data) {
				banks = data;
				var curIndex = -1;
				for(var i=0;i<data.length;i++) {
					if(bankID = data[i][0]) {
						curIndex = i;
					}
					bankNames[i] = data[i][1];
				}
				bankNames.splice(curIndex, 1);
				banks.splice(curIndex, 1);
			}
		});
		$("#bankName").autocomplete({
			source : bankNames,
		});
		$("input[name='batch']").autocomplete({
			source : batches,
		});
		
		$('#transferPacket').click(function() {
			if(validateBatchForm()) {
				console.log('validated..');
				$('#validationMsg').html('');
				$('#validationMsg').hide();
				var bloodStocks = new Array();
				$('#batchTransfer tr').find('input:text[value!=""]').filter(function(){
						if(this.value.length!=0) { 
							return true;
						}
						else { 
							return false;
						}
					}).each(function() {
					bloodStock = new Object();					
					bloodStock.batch = this.value;
					bloodStock.id = $(this).next().val();
					bloodStock.bloodBankId = $('#bankId').val();
					bloodStock.bloodBankName = $('#bankName').val();
					bloodStocks.push(bloodStock);					
				});
				console.log(JSON.stringify(bloodStocks));
				$.ajax({
					headers: { 
				        'Accept': 'application/json',
				        'Content-Type': 'application/json' 
				    },
					url : "/mvc/bank/batchTransferPacket",
					dataType : 'json',
					async: true,
					type    : 'POST',
					data: JSON.stringify(bloodStocks),
					success : function(data) {							
						console.log(data);
						document.getElementById("batchTransferForm").reset();
						alert('Batch transferred successfully !');
					},
					error: function(data) {
						console.log('error: '+data);
					}
				});			
			}
			else {
				$('#validationMsg').show();
				$('#validationMsg').html('Error in Form');
			}
		});
	});
	
	function validateBatchForm() {
		var valid = true;
		$('#batchTransferForm').find('input:text[name="batch"]').each(function() {
			if(this.value !=="null" && this.value.length > 0) {
				var matched = false;
				for(var i=0;i<batchDetails.length;i++) {
					if(batchDetails[i].batch == this.value) {
						matched = true;
						break;
					}
				}
				if(matched == false) {
					valid = false;
					return false;
				}
			}
		});
		return valid;
	}
	
	function validateField(field) {
		if(!checkFieldValue(field)) {
			field.className = 'ui-state-error';
		}
		else {
			field.className = '';
		}
	}
	
	function checkFieldValue(field) {
		if(field.value !=="null" && field.value.length > 0) {
			for(var i=0;i<batchDetails.length;i++) {
				if(batchDetails[i].batch == field.value) {
					$(field).next().val(batchDetails[i].id);
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	function updateBankId(bankName) {
		for(var i=0;i<banks.length;i++) {
			if(bankName == banks[i][1]) {
				$('#bankId').val(banks[i][0]);
				loadBatchDetails(banks[i][0]);
				break;
			}
		}
	}
	
	/* function updateBloodGroup(text) {
		console.log(text.parent);
	} */
	
	function loadBatchDetails(bankID) {
		$.ajax({
			url : "/mvc/bank/loadIssuable?bankId="+bankID,
			dataType : 'json',
			async: false,
			success : function(data) {
				batchDetails = data;
				//console.log(data);
				for(var i=0;i<batchDetails.length;i++) {
					//console.log('--> '+batchDetails[i].bloodGroup);
					batches[i] = batchDetails[i].batch;					
				}
			}
		});
		
	}	
	
</script>
<form id='batchTransferForm'>
	Blood Bank Name: <input type='text' id='bankName' onblur="updateBankId(this.value);"/>
	<input type='hidden' id='bankId' />
	<div style="width: 100%;"><label id="validationMsg" class="ui-state-error" style="visibility: hidden;"></label></div>
	<table id='batchTransfer'>
		<tr>
			<td>Batch: </td>
			<td>
				<input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' />
			</td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td>Batch: </td>
			<td><input type='text' name='batch' onblur="validateField(this)"/><input type='hidden' name='id' /></td>
		</tr>
		<tr>
			<td align="right" colspan="2"><button id="transferPacket">Transfer Packet</button></td>
		</tr>
	</table>
</form>