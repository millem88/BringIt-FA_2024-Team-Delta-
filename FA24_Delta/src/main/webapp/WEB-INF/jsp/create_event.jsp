<!--
	create_event JSP file, gives a form-based interface that enables users to create or edit a new event.
	--It will have fields that allow for entering details and adding items associated with the event.
	Author(s): Darien Dalton, Jamie Mizelle
	Date: 10/13/24
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c"    uri="http://java.sun.com/jsp/jstl/core" %> 

<!--
	Create event JSP, allows users to create an Event, add details and Event Items, and save the Event.
    Also allows Event creator to claim items they are making
	Author(s): Jamie Mizelle
	Date: 11/13/2024
 -->
<!DOCTYPE html>
<html>

<head>
  <%@include file="commonHeadContent.jsp" %>
  <title>Create Event</title>
</head>

<body>
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %> 
	<!-- unique page content begin -->
	
	<!-- <div class="createEventWrapper">  -->
			<!-- <div class="createEventContent">  -->
			<div class="container">
			
				<div class="createEventHeaderContainer">
					<h1 class="createEventHeader">Create Event</h1>
				</div>
				
				<!-- Container that is for displaying any error messages (initially hidden) -->
				<div class="alert alert-danger display-none createEventMessageContainer" role="alert" id="createEventMessageContainer">
				</div>
				
				<!-- This form is for creating or updating an event -->
				<form action="/submitEvent" method="post" id="createEventForm">
					<div class="eventDetailContainer">
					
						<h2 class="createEventHeader">Event Details</h2>
						<!-- Hidden input to pass the event ID -->
						<input type="hidden" id="eventId" name="id" value="${event.id}">
						<!-- Name input -->
						<label for="name">Event Name:</label>
						<input type="text" id="name" name="name" value="${event.name}" required>
						<!-- Description input -->
						<label for="description">Event Description:</label>
						<textarea rows="4" id="description" name="description">${event.description}</textarea>
						<!-- Date/Time input -->
						<label for="dateTime">Event Date/Time:</label>
						<input type="datetime-local" id="dateTime" name="dateTime" value="${event.dateTime}" required>
					
					</div>
				</form>
				
				<!-- Section for managing event items -->
				<div class="eventItemContainer">
					<h2 class="createEventHeader">Event Items</h2>

					<!-- This is a table for listing event items --> 
					<table class="eventItemTable d-none" id="eventItemTable">
                      <thead>
                        <tr class="eventItemsTableHeaderx"> <!--</tr>  WHY WAS THIS HERE?-->
                            <th style="width: 30px;"></th>
                            <th>#</th>
                            <th>Item</th>
                            <th>Hoster Supplied?</th>
                        </tr>
                      </thead>
                      <tbody>
                         
                      </tbody>
                        
					</table>
					<!-- This is a button to add a new item to the event -->
					<button type="button" class="btn btn-secondary addEventItemBtn" id="addEventItemBtn">Add Item</button>
				</div>
				
				<!-- Submit button container -->
				<div class="createEventSubmitButtonContainer">
					<button class="btn btn-primary" id="createEventSubmitButton" type="submit">Submit</button>
				</div>
				
			</div>
	
	<div class="d-none" id="userIdHolder" data-userId="${currentUserId}"></div>
    <!-- unique page content end -->
</body>
	<script src="js/create_event.js"></script>

	<%@include file="commonPostBody.jsp" %>
	
</html>