<style>
@IMPORT url("/css/style.css");
</style>
<div>	
	<p>Event Details of: ${event.organization.organizationName} on ${event.activityDate}</p>
	<table class="oddEven">
		<tr>
			<td>Organization Code:</td>
			<td>${event.organization.organizationCode}</td>
		</tr>
		<tr>
			<td>Activity Date:</td>
			<td>${event.activityDate}</td>
		</tr>
		<tr>
			<td>Activity Location:</td>
			<td>${event.activityLocation.area}</td>
		</tr>
		<tr>
			<td>Requirements</td>
			<td>${event.requirement}</td>
		</tr>
		<tr>
			<td>Fulfillments</td>
			<td>${event.fulfilment}</td>				
		</tr>
		<tr>
			<td>Expected Donor Count: </td>
			<td>${event.expectedDonorCount}</td>
		</tr>
		<tr>
			<td>Blood Bank:</td>
			<td>${event.bloodBank.name}</td>
		</tr>
	</table>
</div>