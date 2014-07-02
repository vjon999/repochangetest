<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="com.websoft.yavni.dto.BloodStock"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Search</title>

<style type="text/css" title="currentStyle">
@import "/css/style.css";

@import "/css/redmond/jquery-ui-1.10.3.custom.css";

@import "/css/jquery.dataTables_themeroller.css";

@import "/css/jquery.dataTables.css";
</style>
<script src="/js/jquery-1.10.2.js" type="text/javascript"></script>
<script src="/js/jquery.dataTables.js" type="text/javascript"></script>
<!-- <script src="/js/jquery.dataTables.columnFilter.js" type="text/javascript"></script> -->

<script type="text/javascript">
	$(function() {
		$(document).ready(function() {
			/* $('#searchGrid').dataTable({
				 "bAutoWidth": false,
				 "aaSorting": [[1,'asc'],[2,'asc'],[3,'desc']],
			}).columnFilter(); */
			$('#searchGrid').dataTable({
				"bJQueryUI" : true,
				"sPaginationType" : "full_numbers"
			});
			$('#searchGrid2').dataTable({
				"bJQueryUI" : true,
				"sPaginationType" : "full_numbers"
			});
			

			$('#logoutTag').on('click', function() {
				location.href = '/mvc/logout';
			});
		});

	});
</script>

</head>

<body>
	<%@ include file="/Head.jsp"%>
	<table style="width: 100%; background-color: #d9dee2;">
		<tr style="background-color: #d9dee2;">
			<td><div style="font-size: 150%;">Welcome ${user.firstName}</div></td>
			<td style="text-align: right;">
				<p id="logoutTag" style="height: 75%; cursor: pointer; color: blue;">
					<u>logout</u>
				</p>
			</td>
		</tr>
	</table>
	<div style="height: 700px;">
		
		<table id="searchGrid" style="min-width: 100%;">
			<thead>
				<tr>
					<th>Blood Group</th>
					<th>Date</th>
					<th>Blood Bank Name</th>
					<th>Pin Code</th>
					<th>Address</th>
				</tr>
			</thead>
			<!-- <tfoot>
					<tr>			
						<th>Blood Group</th>
						<th>Date</th>
						<th>Blood Bank Name</th>
						<th>Pin Code</th>
						<th>Address</th>
					</tr>
				</tfoot> -->
			<tbody>
				<%
					if (null != request.getAttribute("bloodPackets")) {
				%>
				<%
					for (BloodStock bloodPacket : (List<BloodStock>) request.getAttribute("bloodPackets")) {
				%>
				<tr>
					<td><%=bloodPacket.getBloodGroup()%></td>
					<td><%=bloodPacket.getReceivedDate()%></td>
					<td><%=bloodPacket.getBloodBankName()%></td>
					<td><%=bloodPacket.getBloodBankPin()%></td>
					<td><%=bloodPacket.getBloodBankAddressLine1()%></td>
				</tr>
				<%
					}
					}
				%>
			</tbody>
		</table>
		<br/>
		<table id="searchGrid2" style="min-width: 100%;">
			<thead>
				<tr>
					<th>Blood Group</th>
					<th>Count</th>
					<th>Blood Bank Name</th>
					<th>Date</th>
					<th>Address</th>
				</tr>
			</thead>
			<tbody>
				<%
					if (null != request.getAttribute("searchResults")) {
				%>
				<%
					for (Object[] bloodPacket : (List<Object[]>) request
								.getAttribute("searchResults")) {
				%>
				<tr>
					<td><%=bloodPacket[0]%></td>
					<td><%=bloodPacket[1]%></td>
					<td><%=bloodPacket[2]%></td>
					<td><%=bloodPacket[3]%></td>
					<td><%=bloodPacket[4]%></td>
				</tr>
				<%
					}
				}
				%>
			</tbody>
		</table>
	</div>
</body>
<%@ include file="/Footer.jsp"%>
</html>