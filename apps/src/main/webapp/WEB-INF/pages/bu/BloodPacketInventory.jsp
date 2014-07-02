<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.websoft.yavni.dto.RolesTypes"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/Head.jsp"%>
<script>
	var DEF_PACKET_WIDTH = 400;
	var bankID = '${user.bloodBank.bankId}';
	$(function() {
		$("#tabs").tabs();
		$(":button, :input[type=submit], :input[type=reset]").button().click(function(event) {
			event.preventDefault();
		});
		
		initDataTables();
		
		$("#addBloodPacketDialog").dialog({
			width : DEF_PACKET_WIDTH,
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 1000
			},
			hide : {
				effect : "explode",
				duration : 1000
			},
		});

		$("#issuePacketDialog").dialog({
			width : 500,
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 1000
			},
			hide : {
				effect : "explode",
				duration : 1000
			}
		});
		$("#batchTransferDialog").dialog({
			width : 500,
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 1000
			},
			hide : {
				effect : "explode",
				duration : 1000
			}
		});
		$("#removePacket").click(function() {
			$("#issuePacketDialog").dialog("open");
		});		
		$("#addPacket").click(function() {
			cleanPacketForm();
			$("#addBloodPacketDialog").dialog("open");
		});
		$("#batchTransferBtn").click(function() {
			$("#batchTransferDialog").dialog("open");
		});
		$('#logoutTag').on('click', function() {
			location.href='j_spring_security_logout';
		});
		$("#reload").click(function() {
			$("#bloodBalance").dataTable().fnReloadAjax();
		});
	});

	function autoReload() {
		refreshTable('#bloodBalance', "/mvc/bank/inventory?bankId=${user.bloodBank.bankId}");
		setTimeout(function() {
			autoReload();
		}, 30000);
	}
	
	function issuePacket(bloodGroup, btn) {
		$("#issuePacketDialog").dialog("open");
		var sel = $('#removePacketForm').find('#bloodGroup')[0];
		for(var i=0;i<sel.length;i++) {
			if(sel.options[i].value == bloodGroup) {
				sel.selectedIndex = i;				
				break;
			}
		}
		sel.onchange();
		var bloodType = $(btn).parent().parent().find(':nth-child(2)')[0].innerHTML;
		sel = $('#removePacketForm').find('#bloodType')[0];
		for(var i=0;i<sel.length;i++) {
			if(sel.options[i].value == bloodType) {
				sel.selectedIndex = i;				
				break;
			}
		}
		sel.onchange();
	}
</script>
<script src="/js/yavni/BloodBankUser.js"></script>
<script src="/js/yavni/bank/Verification.js"></script>
</head>
<body>
	<div style="background-color: #d9dee2;">
		<table style="width: 100%; background-color: #d9dee2;">
			<tr style="background-color: #d9dee2;">
				<td><div style="font-size: 20px;">Welcome ${sessionScope["user"].firstName}</div></td>
				<td style="text-align: right;">
					<p id="logoutTag" style="height: 75%; cursor: pointer; color: blue;">Logout</p>
				</td>
			</tr>
		</table>
	</div>

	<p>Blood Packet Inventory: (${user.bloodBank.name } Branch)</p>

	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">Blood Packets Add/Issue</a></li>
			<c:if test="${user.hasRole('ROLE_CMO')}">
				<li><a href="#tabs-2">All Records</a></li>
			</c:if>
			<c:if test="${user.hasRole('ROLE_VERIFIER')}">
				<li><a href="#tabs-3">Verification</a></li>
			</c:if>
			<c:if test="${user.hasRole('ROLE_APPROVER')}">
				<li><a href="#tabs-4">Approval</a></li>
			</c:if>
			<c:if test="${user.hasRole('ROLE_CMO')}">
				<li><a href="#tabs-5">Batch Transfer</a></li>
			</c:if>
		</ul>

		<div id="tabs-1">
			<table id="bloodBalance" style="width: 100%;">
				<thead>
					<tr>
						<th>Blood Group</th>
						<th>Blood Type</th>
						<th>Packets</th>
						<th>Recent-In</th>
						<th>Oldest-In</th>
						<th>Issue</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Blood Group</th>
						<th>Blood Type</th>
						<th>Packets</th>
						<th>Recent-In</th>
						<th>Oldest-In</th>
						<th id="hideMe" class="hideColumnFilter"></th>
					</tr>
				</tfoot>
			</table>
			<button id="addPacket">Add Blood Packet</button>
			<button id="removePacket">Issue Blood Packet</button>
			<button id="reload">Reload</button>
		</div>
		<c:if test="${user.hasRole('ROLE_CMO')}">
		<div id="tabs-2">
			<table id="bloodRecords" style="width: 100%;">
				<thead>
					<tr>
						<th>Id</th>
						<th>Batch</th>
						<th>Blood Group</th>
						<th>Blood Type</th>
						<th>Received Date</th>
						<th>Issued Date</th>
						<th>Verified</th>
						<th>Verified By</th>
						<th>Verified Date</th>
						<th>Approved</th>
						<th>Approved By</th>
						<th>Approved Date</th>
					</tr>
				</thead>
			</table>
		</div>
		</c:if>
		<c:if test="${user.hasRole('ROLE_VERIFIER')}">
			<div id="tabs-3">
				<table id="verifiableRecords" style="width: 100%;">
					<thead>
						<tr>
							<th>Id</th>
							<th>Batch</th>
							<th>Blood Group</th>
							<th>Blood Type</th>
							<th>Received Date</th>
							<th>Issued Date</th>
							<th>Verify</th>
							<th>Reject</th>
							<th>Verifier Remarks</th>
						</tr>
					</thead>
				</table>
				<table><tr><td align="right">
					<button id="verify" onclick="verify();">Verify</button>
				</td></table>
			</div>
		</c:if>

		<c:if test="${user.hasRole('ROLE_APPROVER')}">
			<div id="tabs-4">
				<table id="approvableRecords" style="width: 100%;">
					<thead>
						<tr>
							<th>Id</th>
							<th>Batch</th>
							<th>Blood Group</th>
							<th>Blood Type</th>
							<th>Received Date</th>
							<th>Issued Date</th>
							<th>Verified</th>
							<th>Verified By</th>
							<th>Verified Date</th>
							<th>Approve</th>
							<th>Reject</th>
							<th>Approver Remarks</th>
						</tr>
					</thead>
				</table>
				<table><tr><td align="right">
					<button id="approve" onclick="approve();">Approve/Reject</button>
				</td></tr></table>
			</div>
		</c:if>
		<c:if test="${user.hasRole('ROLE_CMO')}">
			<div id="tabs-5" title="Batch Transfer" style="width: 25%;">
				<jsp:include page="../BatchTransfer.jsp"></jsp:include>
			</div>
		</c:if>
	</div>







	<div id="addBloodPacketDialog" title="Add Blood Packet"><jsp:include page="../AddBloodPacket.jsp"></jsp:include></div>
	<div id="issuePacketDialog" title="Issue Packet"><jsp:include page="../IssueBloodPacket.jsp"></jsp:include></div>
	<%-- <c:if test="${user.hasRole('ROLE_CMO')}">
		<div id="batchTransferDialog" title="Batch Transfer"><jsp:include page="../BatchTransfer.jsp"></jsp:include></div>
	</c:if> --%>
	
</body>
<%@ include file="/Footer.jsp"%>
</html>