<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!--
	Event Details JSP, allows users to view details of an event
	they are in, or want to join.
	Author(s): Jamie Mizelle
	Date: 11/13/2024
 -->
<!DOCTYPE html>
<html>

<head>
  <%@include file="commonHeadContent.jsp" %>
  <title>Event Details</title>
</head>

<body>
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %> 
	<!-- unique page content begin -->
	
	<!-- <div class="createEventWrapper">  -->
			<div class="container">
			
				<div class="createEventHeaderContainer">
					<h1 class="createEventHeader">View Event</h1>
				</div>
				
				<div class="alert alert-danger display-none eventDetailsMessageContainer" role="alert" id="eventDetailsMessageContainer">
				</div>
				
				<div class="modal" tabindex="-1" id="leaveModal">
  					<div class="modal-dialog">
    					<div class="modal-content">
      						<div class="modal-header">
        						<h5 class="modal-title">Are you sure you want to leave this event?</h5>
        						<button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
      						</div>
      						<div class="modal-footer">
        						<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
        						<button type="button" class="btn btn-success" id="event-details-leave">Yes</button>
      						</div>
    					</div>
  				  	</div>
				</div>
				
				<div class="modal" tabindex="-1" id="deleteFreeformItemModal">
  					<div class="modal-dialog">
    					<div class="modal-content">
      						<div class="modal-header">
        						<h5 class="modal-title">Are you sure you want to delete this item??</h5>
        						<button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
      						</div>
      						<div class="modal-footer">
        						<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
        						<button type="button" class="btn btn-success" id="freeform-item-delete">Yes</button>
      						</div>
    					</div>
  				  	</div>
				</div>
				
				<form action="/submitEvent" method="post" id="createEventForm">
					<div class="eventDetailContainer">
						<h2 class="createEventHeader">Event Details</h2>
						<!-- Hidden input to pass the event ID -->
						<input type="hidden" id="eventId" name="id" value="${event.id}">
						<!-- Name input -->
						<label for="name">Event Name:</label>
						<input type="text" id="name" name="name" value="${event.name}" readonly>
						<!-- Description input -->
						<label for="description">Event Description:</label>
						<textarea rows="4" id="description" name="description" readonly>${event.description}</textarea>
						<!-- Date/Time input -->
						<label for="dateTime">Event Date/Time:</label>
						<input type="datetime-local" id="dateTime" name="dateTime" value="${event.dateTime}" readonly>
					</div>
				</form>
				<div class="eventItemContainer">
					<h2 class="createEventHeader">Event Items</h2>

					<table class="eventItemTable" id="eventItemTable">
                      <thead>
                        <tr class="eventItemsTableHeaderx"> <!--</tr>  WHY WAS THIS HERE?-->
                            <th>#</th>
                            <th>Item</th>
                            <th>Claimed?</th>
                        </tr>
                      </thead>
                      <tbody>
                         <c:forEach var="eventItem" items="${eventItems}" varStatus="myIndex">
        					<tr class="eventItemTableRow" data-eventItemId="${eventItem.id}">  
                
                              <td class="eventItemTableData">
                                <label for="eventItemInput" class="eventItemLable">${myIndex.index + 1}</label> 
                              </td>

                              <td class="eventItemTableData">
                                <input type="text" class="eventItemDataInput" name="eventItemInput" value="${eventItem.name}" readonly>
                              </td>

                              <td class="eventItemTableData" >
                                
                                  <c:if test = "${eventItem.userId > 0}">
                                  
                                  	<c:if test = "${eventItem.userId == currentUserId}">
       								  	<button type="button" class="btn btn-secondary btn-sm unclaimButton" onclick="handleUnclaimButtonClicked(this)">Unclaim</button>
       								</c:if> 
       								
       								<c:if test = "${!(eventItem.userId == currentUserId)}">
       								  	<p style="margin-bottom: 0;">
       								  		${eventItem.assignedUserFirstName} ${eventItem.assignedUserLastName}
       									</p>
       								</c:if> 
    							  </c:if>   
                                  
                                  <c:if test = "${eventItem.userId <= 0}">
       								
       								  <c:if test = "${!(eventItem.userId == currentUserId) && userInEvent}">
       								  	<button type="button" class="btn btn-primary btn-sm claimButton" onclick="handleClaimButtonClicked(this)">Claim</button>
       								  </c:if> 
       								  
       								  <c:if test = "${!userInEvent}">
       									<p style="margin-bottom: 0;">
       										No
       									</p>
    								  </c:if>  
       								  
    							  </c:if>  
                              </td>
                        	</tr>
   						 </c:forEach>
                      </tbody>
                        
					</table>
					
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
        					<tr class="eventFreeformItemTableRow existingItem" data-eventFreeformItemId="${eventFreeformItem.id}">  
							  
							    <c:if test = "${eventFreeformItem.userId == currentUserId}">
								  <td class="eventItemTableData deleteFreeformItem" data-toggle="modal" data-target="#deleteFreeformItemModal">							   
       								❌
       							  </td>
       						    </c:if> 
                			  
                			  <c:if test = "${!(eventFreeformItem.userId == currentUserId)}">
								  <td class="eventItemTableData">							   

       							  </td>
       						 </c:if>
                			  
                			  
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
					
					
					<c:if test = "${userInEvent}">
	       				<div class="my-1 buttons">
							<button type="button" class="btn btn-primary addFreeformButton" onclick="handleAddFreeformItem()" id="addFreeformItemButton">Add Item</button>
							<button type="button" disabled class="btn btn-secondary saveFreeformButton" id="saveFreeformButton" onclick="tryToSaveFreeformEventItems()">Save</button>
						</div>
    				</c:if>
					
					
				</div>
				
				
				
				
				
				<c:if test = "${userInEvent}">
       				<p>
       					<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#leaveModal" id="leave-event-btn">Leave Event</button>
       				</p>
    			</c:if>  
    			<c:if test = "${!userInEvent}">
       				<p>
       					<button type="button" class="btn btn-success" id="event-details-join" >Join Event</button>
       				</p>
    			</c:if> 
				
			</div>
		<!-- </div> -->
	
	<div class="d-none" id="userInEventHolder" data-userInEvent="${userInEvent}"></div>
	<div class="d-none" id="userName" data-userName="${userFirstName} ${userLastName}"></div>
	<div class="d-none" id="numOfFreeformItems" data-numOfFreeformItems="${eventFreeformItems.size()}"></div>
	<div class="d-none" id="userIdHolder" data-userId="${currentUserId}"></div>
    <!-- unique page content end -->
</body>
	<script src="js/event_details.js"></script>

	<%@include file="commonPostBody.jsp" %>
	
</html>