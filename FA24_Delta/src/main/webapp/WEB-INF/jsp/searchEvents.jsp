
<!--
	searchEvents JSP file, makes a page for "Search Events" functionality.
	--Will be used to display different events that users are able to join.
	Author(s): Abdinasir Aidrus (Nas), Darien Dalton
	Date: 10/25/24
 -->


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>

<head>
  <%@include file="commonHeadContent.jsp" %>
  <title>Search Events</title>
</head>

<body>
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %> 
	<!-- unique page content begin -->
	
	<!-- Bootstrap container for responsive breakpoints (Nas)  -->
	<section class="container">

		
		<!-- Row wrapper for columns (Nas) -->
		<div class="row">
		
		<!-- Columns for the grid layout (Nas)  -->
			<div class="col-12 col-md-6 col-lg-4 my-3 d-flex justify-content-center">
				<h1 class="createEventHeader">Joinable Events</h1>
			</div>
        </div>
		
        <div class="row" id="joinable-events">
        	
        </div>
    </section >
    
    
    <!-- unique page content end -->
</body>
	
	<script src="js/searchEvents.js"></script>
	
	<%@include file="commonPostBody.jsp" %>
	
</html>