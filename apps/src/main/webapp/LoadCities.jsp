<html>
<script src="./resources/js/jquery-1.11.1.js"></script>
<script src="./resources/js/jquery-ui-1.10.3.custom.js"></script>
<script src="./resources/js/jquery.dataTables.js" type="text/javascript"></script>
<script src="./resources/js/jquery.dataTables.columnFilter.js" type="text/javascript"></script>
<script src="./resources/js/js-image-slider.js" type="text/javascript"></script>
<script src="./resources/js/jquery.timepicker.js"></script>

<style type="text/css" title="currentStyle">
	@import "./resources/css/cupertino/jquery-ui-1.10.3.custom.css";
	@import "./resources/css/js-image-slider.css";
	@import "./resources/css/jquery.timepicker.css";
</style>

<script type="text/javascript">
$(function() {
	$.ajax({
		url : "./json/LOAD_CITIES?appCode=yavni",
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