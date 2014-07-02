<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/Head.jsp"%>
<link rel="stylesheet" href="/css/redmond/jquery-ui-1.10.3.custom.css" />
<script src="/js/jquery-1.10.2.js"></script>
<script src="/js/jquery-ui-1.10.3.custom.js"></script>
<script>
$(function() {
    $( "#menu" ).menu();
  });
</script>
<style>
  .ui-menu { width: 150px; }
  </style>
</head>
<body>
	<div class="ui-widget ui-widget-content ui-corner-all" style="width: 80%; float: right; right: 0px;">
		<div class="ui-widget-header">
			<div>
				<b>Tag: </b>
			</div>
		</div>
	</div>
	<!-- <div class="ui-widget ui-widget-content ui-corner-all" style="width: 19%; float: left; right: 0px;">
		<div class="ui-widget-header"> -->
			<ul id="menu">
			  <li class="ui-state-disabled"><a href="#">Aberdeen</a></li>
			  <li><a href="#">Ada</a></li>
			  <li><a href="#">Adamsville</a></li>
			  <li><a href="#">Addyston</a></li>
			  <li>
			    <a href="#">Delphi</a>
			    <ul>
			      <li class="ui-state-disabled"><a href="#">Ada</a></li>
			      <li><a href="#">Saarland</a></li>
			      <li><a href="#">Salzburg</a></li>
			    </ul>
			  </li>
			  <li><a href="#">Saarland</a></li>
			  <li>
			    <a href="#">Salzburg</a>
			    <ul>
			      <li>
			        <a href="#">Delphi</a>
			        <ul>
			          <li><a href="#">Ada</a></li>
			          <li><a href="#">Saarland</a></li>
			          <li><a href="#">Salzburg</a></li>
			        </ul>
			      </li>
			      <li>
			        <a href="#">Delphi</a>
			        <ul>
			          <li><a href="#">Ada</a></li>
			          <li><a href="#">Saarland</a></li>
			          <li><a href="#">Salzburg</a></li>
			        </ul>
			      </li>
			      <li><a href="#">Perch</a></li>
			    </ul>
			  </li>
			  <li class="ui-state-disabled"><a href="#">Amesville</a></li>
			</ul>
		<!-- </div>
	</div> -->
</body>
</html>