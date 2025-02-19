<!-- Author: Lisa Gehrt -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>

<head>
  <%@include file="commonHeadContent.jsp" %>
  <title>My Events</title>
</head>

<body>
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %> 
	<!-- unique page content begin -->
	
	
	<section class="container">
	
		<div class="row">
			<div class="col-12 col-md-6 col-lg-4 my-3 d-flex justify-content-center">
				<h1 class="createEventHeader">My Events</h1>
			</div>
        </div>
		
        <div class="row" id="joinable-events">
        	
        </div>
    </section >
    
    
    <!-- unique page content end -->
</body>
	
	<script src="js/myEvents.js"></script>
	
	<%@include file="commonPostBody.jsp" %>
	
</html>