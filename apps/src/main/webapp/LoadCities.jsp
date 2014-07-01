<html>
<script src="./js/jquery-1.10.2.js"></script>
<script src="./js/jquery-ui-1.10.3.custom.js"></script>
<script src="./js/jquery.dataTables.js" type="text/javascript"></script>
<script src="./js/jquery.dataTables.columnFilter.js" type="text/javascript"></script>
<script src="./js/js-image-slider.js" type="text/javascript"></script>
<script src="./js/jquery.timepicker.js"></script>

<script type="text/javascript">
$(function() {
	$.ajax({
		url : "./json?action=LOAD_CITIES",
		dataType : 'json',
		async: false,
		success : function(data) {
			cities = data;
			console.log(cities);
		}
	});
	$("#dob").datepicker({ dateFormat: 'dd/mm/yy' });
	$("#lastDonatedDate").datepicker({ dateFormat: 'dd/mm/yy' });
	$( "#city" ).autocomplete({
		source: cities,
	});
});
</script>

Load Cities

</html>