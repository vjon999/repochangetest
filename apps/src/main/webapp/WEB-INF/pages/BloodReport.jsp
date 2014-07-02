<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/Head.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Blood Report</title>
<script type="text/javascript">
$(function() {
	$("#tabs").tabs();
	$("#datepicker").datepicker({
		dateFormat: 'dd/mm/yy',
		onSelect: function(selected,evnt) {
			refreshTable('#bloodReportGrid', "/mvc/bank/generateBloodReport?date="+$(this).val()+'00:00');
    	}
	});
	$("#datepicker").datepicker("setDate", new Date());
	$('#bloodReportGrid').dataTable({
		"bJQueryUI" : true,
		"bProcessing" : true,
		"sAjaxSource" : "/mvc/bank/generateBloodReport?date="+new Date().toUTCString(),
		"aoColumns" : [ {
			"mData" : "bankName"
		}, {
			"mData" : "bloodGroup"
		}, {
			"mData" : "bloodType"
		}, {
			"mData" : "bloodInCount"
		},{
			"mData" : "bloodOutCount"
		}]
	}).columnFilter();
});

function exportCSV() {
	var date = $('#datepicker').val();
	window.open('/mvc/stream/exportBloodReport?date='+date+'00:00', '_blank', '', '');
}
</script>
</head>
<body>
<div id="tabs">
		<ul>
			<li><a href="#tabs-1">Blood Report </a></li>
		</ul>
		<div id="tabs-1">
			Select Report Date: <input type="text" id="datepicker" name="receivedDate" />
			<img src="/images/icons/excel_16.png" onclick="exportCSV()" alt='Export to CSV'/>
			<table id="bloodReportGrid" style="min-width: 100%;">
				<thead>
					<tr>
						<th>Bank Name</th>
						<th style="width: 10%;">Blood Group</th>						
						<th>Blood Type</th>
						<th>Blood In</th>
						<th>Blood Out</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Bank Name</th>
						<th style="width: 10%;">Blood Group</th>						
						<th>Blood Type</th>
						<th>Blood In</th>
						<th>Blood Out</th>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</body>
</html>