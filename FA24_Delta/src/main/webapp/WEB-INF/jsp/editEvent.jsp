<!-- Authors: Abdinasir Aidrus (Nas) -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>

<head>
  <%@include file="commonHeadContent.jsp" %>
  <title>Edit Event</title>
</head>

<body>
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %> 
	<!-- unique page content begin -->
	<section class="container">
	<div class="createEventWrapper">
			<div class="createEventContent">
				<h1 class="createEventHeader">Edit Event</h1>
				
				
				<div class="alert alert-danger display-none createEventMessageContainer" role="alert" id="editEventMessageContainer">
				</div>

				

				
				<div class="modal" tabindex="-1" id="deleteItemModal">
  					<div class="modal-dialog">
    					<div class="modal-content">
      						<div class="modal-header">
        						<h5 class="modal-title">Are you sure you want to delete this item??</h5>
        						<button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
      						</div>
      						<div class="modal-footer">
        						<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
        						<button type="button" class="btn btn-success" id="event-item-delete">Yes</button>
      						</div>
    					</div>
  				  	</div>
				</div>
				
				<div class="modal" tabindex="-1" id="deleteEventModal">
  					<div class="modal-dialog">
    					<div class="modal-content">
      						<div class="modal-header">
        						<h5 class="modal-title">Are you sure you want to delete this event??</h5>
        						<button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
      						</div>
      						<div class="modal-footer">
        						<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
        						<button type="button" class="btn btn-success" id="event-delete">Yes</button>
      						</div>
    					</div>
  				  	</div>
				</div>
				
				<!-- Form that submits data to the /updateEvent controller  (Nas)  -->

				<form action="/updateEvent" method="post" id="editEventForm">
					<div class="eventDetailContainer">
						<h2 class="createEventHeader">Event Details</h2>
						<!-- Hidden input to pass the event ID -->
						<input type="hidden" id="eventId" name="id" value="${event.id}">
						<!-- Hidden input to pass the event ID -->
						<input type="hidden" id="eventOwningUserId" name="owningUserId" value="${event.owningUserId}">
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
				<div class="eventItemContainer">
					<h2 class="createEventHeader">Event Items</h2>

					<table class="eventItemTable" id="eventItemTable">
                      <thead>
                        <tr class="eventItemsTableHeaderx"> <!--</tr>  WHY WAS THIS HERE?-->
                            <th style="width: 30px;"></th>
                            <th>#</th>
                            <th>Item</th>
                            <th>Hoster Supplied?</th>
                        </tr>
                      </thead>
                      <tbody>
                      <!-- Loop through the event items and display them (Nas) -->
                          <c:forEach var="eventItem" items="${eventItems}" varStatus="myIndex">
        					<tr class="eventItemTableRow" data-assignedUserId="${eventItem.userId}">  


	                		   <td class="eventItemTableData deleteTableRow" data-toggle="modal" data-target="#deleteItemModal">
	                                ❌
	                           </td>
	                           
	                           
                              <td class="eventItemTableData">
                                <label for="eventItemInput" class="eventItemLable">${myIndex.index + 1}</label> 
                              </td>

                              <td class="eventItemTableData">
                                <input type="text" class="eventItemDataInput" name="eventItemInput" value="${eventItem.name}" >
                              </td>

                              <td class="eventItemTableData" >
                                
                                  <c:if test = "${eventItem.userId > 0}">
                                  	<c:if test="${eventItem.userId == currentUserId}" >
                                  		
                                  		<input type="checkbox" checked class="eventItemCheckbox" name="eventItemCheckbox">
                                  	
                                  	</c:if>
                                  	<c:if test="${!(eventItem.userId == currentUserId)}" >
                                  		
                                  			<p style="margin-left: 1rem;">
       								  			${eventItem.assignedUserFirstName} ${eventItem.assignedUserLastName}
       										</p>
                                  	
                                  	</c:if>
       							
    							  </c:if>   
                                  
                                  <c:if test = "${eventItem.userId <= 0}"> 
       									<input type="checkbox" class="eventItemCheckbox" name="eventItemCheckbox">
    							  </c:if>  
                              </td>
                        	</tr>
   						 </c:forEach>
                      </tbody>
                        
					</table>
					<button class="btn btn-secondary addEventItemBtn" id="addEventItemBtn">Add Item</button>
					
				</div>
				
				
				<div class="eventFreeformItemContainer">
					<h2 class="createEventHeader">Guest Provided Items</h2>

					<table class="eventItemTable" id="eventFreeformItemTable">
                      <thead>
                        <tr class="eventItemsTableHeaderx"> <!--</tr>  WHY WAS THIS HERE?-->
                        	<th style="width: 30px;"></th>
                            <th>#</th>
                            <th>Item</th>
                            <th>User</th>
                        </tr>
                      </thead>
                      <tbody>
                         <c:forEach var="eventFreeformItem" items="${eventFreeformItems}" varStatus="myIndex">
        					<tr class="eventFreeformItemTableRow existingItem" data-assignedUserId="${eventFreeformItem.userId}">  
							  

								<td class="eventItemTableData deleteFreeformItem" data-toggle="modal" data-target="#deleteItemModal">							   
       								❌
       							</td>
  
                			  
                			  
                              <td class="eventItemTableData">
                                <label for="eventItemInput" class="eventItemLable">${myIndex.index + 1}</label> 
                              </td>

                              <td class="eventItemTableData">
                                <input type="text" class="eventItemDataInput" name="eventItemInput" value="${eventFreeformItem.name}" readonly>
                              </td>

                              <td class="eventItemTableData" >
                                <p style="margin-bottom: 0;">
       								${eventFreeformItem.assignedUserFirstName} ${eventFreeformItem.assignedUserLastName}
       							</p>
                                  
                              </td>
                              
                              
                        	</tr>
   						 </c:forEach>

                      </tbody>
                        
					</table>
					
					
					
				</div>
				
				
				
				<div class="createEventSubmitButtonContainer">
					<button class="btn btn-primary" id="editEventSubmitButton" type="submit">Update</button>
					<button class="btn btn-danger" id="editEventDeleteButtom" data-toggle="modal" data-target="#deleteEventModal" type="submit">Delete</button>
				</div>
			</div>
		</div>
	</section>
	<div class="d-none" id="numOfEventItems" data-numOfEventItems="${eventItems.size()}"></div>
	<div class="d-none" id="userIdHolder" data-userId="${currentUserId}"></div>
    <!-- unique page content end -->
</body>
	<script src="js/update_event.js"></script>
	<%@include file="commonPostBody.jsp" %>
</html>