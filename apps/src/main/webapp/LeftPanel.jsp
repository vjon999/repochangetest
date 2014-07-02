<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="lm_bg">
  	<div class="lm_01"><div id="style_00"><a href="/mvc/json/login">Admin Home</a></div></div>      	
      <div class="lm_03">Navigate</div>
      <div class="lm_02"><a href="/mvc/su/resource/CreateUser" class="ho_01">Create User</a></div>
      <div class="lm_02"><a href="/mvc/su/resource/AddBloodBank" class="ho_01">Add Blood Bank</a></div>
      <div class="lm_02"><a href="/mvc/search" class="ho_01">Search</a></div>
      <div class="lm_02"><a href="/mvc/su/resource/CreateDonor" class="ho_01">Add Donor</a></div>
      <div class="lm_02"><a href="/mvc/su/resource/AddOrganization" class="ho_01">Add Organization</a></div>
      <div class="lm_02" id='createEvent' onclick='handleMenuClick(this.id);'><a href="#" class="ho_01">Create Event</a><!-- <a href="/mvc/resource/CreateEvent" class="ho_01">Create Event</a> --></div>
      <div class="lm_02"><a href="/mvc/resource/BloodReport" class="ho_01">Blood Report</a></div>
      <div class="lm_02"><a href="<c:url value="j_spring_security_logout" />" class="ho_01" > Logout</a></div>
</div>     
