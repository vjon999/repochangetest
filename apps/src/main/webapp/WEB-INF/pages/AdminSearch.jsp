<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- <jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0"> --%>
<html>
<head>
<%@ include file="/Head.jsp"%>
<script>
	$(function() {
		$("#tabs").tabs();

		$('#packetGrid').dataTable({
			"bJQueryUI" : true,
			"bProcessing" : true,
			"sAjaxSource" : "/mvc/json/loadBloodPacketsByGroup",
			"aoColumns" : [ {
				"mData" : "bloodGroup"
			}, {
				"mData" : "bankName"
			}, {
				"mData" : "count"
			}, {
				"mData" : "recent"
			}, {
				"mData" : "oldest"
			} ]
		});

		$('#userGrid').dataTable({
			"bJQueryUI" : true,
			"bProcessing" : true,
			"sAjaxSource" : "/mvc/json/loadUsers",
			"aoColumns" : [ {
				"mData" : "userName", "mRender": function ( data, type, full ) {
                  return '<a href=\'/mvc/'+data+'/\'>'+data+'</a>';
                }
			}, {
				"mData" : "firstName"
			}, {
				"mData" : "lastName"
			}, {
				"mData" : "roleStr"
			} ],
		});

		$('#bankGrid').dataTable({
			"bJQueryUI" : true,
			"bProcessing" : true,
			"sAjaxSource" : "/mvc/json/loadBanks",
			"aoColumns" : [ {
				"mData" : "name"
			}, {
				"mData" : "cmo"
			}, {
				"mData" : "address.area"
			}, {
				"mData" : "address.addressLine1"
			}, {
				"mData" : "address.pin"
			}, ]
		});

		$('#orgGrid').dataTable({
			"bJQueryUI" : true,
			"bProcessing" : true,
			"sAjaxSource" : "/mvc/json/loadOrganizations",
			"aoColumns" : [ {
				"mData" : "organizationName"
			}, {
				"mData" : "campDate"
			}, ]
		});
	});
</script>
</head>
<body>
	<div id="tabs">
		<ul>
			<%-- <c:if test = "${user.roleStr == 'superuser'}"><li><a href="#tabs-1">Blood Packets</a></li></c:if> --%>
			<li><a href="#tabs-1">Blood Packets</a></li>
			<li><a href="#tabs-2">Users</a></li>
			<li><a href="#tabs-3">Banks</a></li>
			<li><a href="#tabs-4">Organizations</a></li>
		</ul>
		<%-- <c:if test = "${user.roleStr == 'superuser'}"> --%>
		<div id="tabs-1">
			<table id="packetGrid" style="min-width: 100%;">
				<thead>
					<tr>
						<th style="width: 10%;">Blood Group</th>
						<th>Bank Name</th>
						<th>Count</th>
						<th>Recent</th>
						<th>Oldest</th>
					</tr>
				</thead>
			</table>
		</div>
		<%-- </c:if> --%>
		<div id="tabs-2">
			<table id="userGrid" style="min-width: 100%;">
				<thead>
					<tr>
						<th>User Name</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Roles</th>
					</tr>
				</thead>
			</table>
		</div>

		<div id="tabs-3">
			<table id="bankGrid" style="min-width: 100%;">
				<thead>
					<tr>
						<th>Bank Name</th>
						<th>Chief Medical Officer</th>
						<th>Area</th>
						<th>Address</th>
						<th>Pin Code</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="tabs-4">
			<table id="orgGrid" style="min-width: 100%;">
				<thead>
					<tr>
						<th>Organization Name</th>
						<th>Camp Date</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>

</body>
</html>