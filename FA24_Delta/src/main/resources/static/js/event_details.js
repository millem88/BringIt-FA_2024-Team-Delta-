// JS for event_details.jsp, this handles all frontend functionality
// for event details, including POSTing to backend endpoints
// Author(s): Jamie Mizelle
// Date: 11/13/2024


const userInEvent = document.getElementById("userInEventHolder").getAttribute('data-userInEvent') === "true";
const userName = document.getElementById("userName").getAttribute('data-userName');
const currentUserId = Number(document.getElementById("userIdHolder").getAttribute('data-userId'));
const currentEventId = Number(document.getElementById("eventId").value);
let numOfFreeformItems = Number(document.getElementById("numOfFreeformItems").getAttribute('data-numOfFreeformItems'));
let currentFreeformTarget = null;

if(!userInEvent)
{
	document.getElementById("event-details-join").addEventListener("click", function(event)
	{
		console.log("join clicked");
	

	
		tryToJoinEvent();
    });
}




const tryToJoinEvent = async function()
{
	let payload = new FormData;
	payload.append("eventId", currentEventId);
	
	const settings = {
	  method: 'POST',
	  body: payload,
  	};
  
  
  	// fetch to the "./fetchEvents" controller endpoint instead of the dummy data.
  	try
  	{
	  	const response = await fetch("./attemptJoinEvent", settings);
	  	const data = await response.json();
	  	handleTryToJoinResponse(data);
	  
  	}
	catch(err)
	{
		console.error(err);
	}
}


const handleTryToJoinResponse = function(data)
{
	console.log(data);
	
	// display response
	let messageContainer = document.getElementById("eventDetailsMessageContainer");
      
    // check to see if the DAO was able to create EP object
    // otherwise, displays an error on the page
    if (data.lastInsertedId == -1)
    {
      	messageContainer.innerText = "Error, something went wrong trying to join the Event";
      	messageContainer.className = "alert alert-danger eventDetailsMessageContainer";
        console.log("failure");
    }
    else
    {
        console.log("success");
        messageContainer.className = "alert alert-success eventDetailsMessageContainer";
        messageContainer.innerText = "Join Successful! You can see your Events under the `My Events' tab!";
    }
    
    
	// disable join button after successful join
	
	// disable join button after successful join
	document.getElementById("event-details-join").disabled = true;
}


if(userInEvent)
{
	document.getElementById("event-details-leave").addEventListener("click", function(event)
	{
		console.log("leave clicked");
	

	
		tryToLeaveEvent();
	});
}




const tryToLeaveEvent = async function()
{
	$('#leaveModal').modal('hide')
	
	let payload = new FormData;
	payload.append("eventId", currentEventId);
	
	const settings = {
	  method: 'POST',
	  body: payload,
  	};
  
  
  	// fetch to the "./fetchEvents" controller endpoint instead of the dummy data.
  	try
  	{
	  	const response = await fetch("./attemptLeaveEvent", settings);
	  	const data = await response.json();
	  	handleTryToLeaveResponse(data);
	  
  	}
	catch(err)
	{
		console.error(err);
	}
}


const handleTryToLeaveResponse = function(data)
{
	console.log(data);
	
	// display response
	let messageContainer = document.getElementById("eventDetailsMessageContainer");
      
    // check to see if the DAO was able to create EP object
    // otherwise, displays an error on the page
    if (data.errors.length == 0)
    {
		console.log("success");
        messageContainer.className = "alert alert-success eventDetailsMessageContainer";
        messageContainer.innerText = "Leave Successful!";
        
        // disable all claim/unclaim buttons
        let claimAndUnclaimButtons = document.querySelectorAll(".claimButton, .unclaimButton");
        claimAndUnclaimButtons.forEach((button) => {
  			button.disabled = true;
		});
		
      	// disable leave button after successful leave
		document.getElementById("leave-event-btn").disabled = true;
		
		// disable freeform item buttons after leaving
		document.getElementById("addFreeformItemButton").disabled = true;
		document.getElementById("saveFreeformButton").disabled = true;
		
		// remove delete functionality from freeform item 'X's (delete button) TODO:
		
		let deleteFreeformItemXs = document.querySelectorAll(".deleteFreeformItem");
        deleteFreeformItemXs.forEach((x) => {
  			x.dataset.toggle = "";
  			x.dataset.target = "";
  			x.innerHTML = "";
		});
		
		
    }
    else
    {
        messageContainer.innerText = "Error, something went wrong trying to leave the Event";
      	messageContainer.className = "alert alert-danger eventDetailsMessageContainer";
        console.log("failure");
    }
    
	
	
}



const handleClaimButtonClicked = function(target)
{
	console.log("claim clicked");
	
	const closestEventItemId = target.closest(".eventItemTableRow").getAttribute('data-eventItemId');
	
	console.log("closestEventItemId = ", closestEventItemId);
	
	tryToAlterEventItem(closestEventItemId, "claim", target);
}




const handleUnclaimButtonClicked = function(target)
{
	console.log("unclaim clicked");
	
	const closestEventItemId = target.closest(".eventItemTableRow").getAttribute('data-eventItemId');
	
	console.log("closestEventItemId = ", closestEventItemId);
	
	tryToAlterEventItem(closestEventItemId, "unclaim", target);
}


const tryToAlterEventItem = async function(closestEventItemId, action, target)
{
	//$('#leaveModal').modal('hide')

	
	
	
	let payload = new FormData;
	payload.append("eventItemId", closestEventItemId);
	
	if(action === "claim" || action === "unclaim")
	{
		payload.append("action", action);
	}
	else
	{
		return;
	}
	
	const settings = {
	  method: 'POST',
	  body: payload,
  	};
  
  
  	// fetch to the "./fetchEvents" controller endpoint instead of the dummy data.
  	try
  	{
	  	const response = await fetch("./attemptEditEventItem", settings);
	  	const data = await response.json();
	  	handleTryToAlterEventItem(data, action, target);
	  
  	}
	catch(err)
	{
		console.error(err);
	}
}


const handleTryToAlterEventItem = function(data, action, target)
{
	console.log(data);
	
	// display response
	let messageContainer = document.getElementById("eventDetailsMessageContainer");
      
    // check to see if the DAO was able to edit Event Item
    // otherwise, displays an error on the page
    if (data.errors.length == 0)
    {
		console.log("success");
        messageContainer.className = "alert alert-success eventDetailsMessageContainer";
        messageContainer.innerText = `Successfully ${action}ed the Event Item!`;
        
        // swap button to alternate type
        // e.g., "Claim" -> "Unclaim" and vice versa
		let closestTd = target.closest(".eventItemTableData");
		closestTd.innerHTML = "";
		
		
		if(action === "claim")
		{
			// action was to claim, swap to unclaim
			closestTd.innerHTML = '<button type="button" class="btn btn-secondary btn-sm unclaimButton" onclick="handleUnclaimButtonClicked(this)">Unclaim</button>';
		}
		else
		{
			// action was to unclaim, swap to claim
			closestTd.innerHTML = '<button type="button" class="btn btn-primary btn-sm claimButton" onclick="handleClaimButtonClicked(this)">Claim</button>';
		}

    }
    else
    {
        messageContainer.innerText = `Error, something went wrong trying to ${action} the Event Item`;
      	messageContainer.className = "alert alert-danger eventDetailsMessageContainer";
        console.log("failure");
    }
}


const handleAddFreeformItem = function()
{
    
    let tableEle = document.getElementById("eventFreeformItemTable");
    
    const newTableRowHTML = `
        <tr class="eventFreeformItemTableRow">
                            <td class="eventItemTableData deleteFreeformItem" data-toggle="modal" data-target="#deleteFreeformItemModal">
                                ❌
                            </td>
                
                            <td class="eventItemTableData">
                                <label for="eventItemInput" class="eventItemLable">${++numOfFreeformItems}</label> 
                            </td>

                            <td class="eventItemTableData">
                                <input type="text" class="eventItemDataInput" name="eventItemInput">
                            </td>

                            <td class="eventItemTableData" >
                                <p style="margin-bottom: 0;">
       								${userName}
       							</p>
                            </td>
                        </tr>
    `;


    tableEle.insertAdjacentHTML("beforeend", newTableRowHTML);
    document.getElementById("eventItemTable").className = "eventItemTable";
    
    let saveButton = document.getElementById("saveFreeformButton");
    saveButton.disabled = false;
}



document.getElementById("eventFreeformItemTable").addEventListener("click", function(event)
{
    // event.preventDefault();
    
    
    const eventTargetClassName = event.target.className;
    if(eventTargetClassName.includes("deleteFreeformItem"))
    {
        const closestRow = event.target.closest(".eventFreeformItemTableRow");
        console.log(closestRow);
        //closestRow.className = "d-none";
        
        
        currentFreeformTarget = closestRow;
        
        

    }

    
});

document.getElementById("freeform-item-delete").addEventListener("click", function(event)
{
	$('#deleteFreeformItemModal').modal('hide')
	
	console.log("delete clicked");
	if(currentFreeformTarget.className.includes("existingItem"))
    {
		// actually delete item in DB
		console.log("delete in DB");
		const target = currentFreeformTarget;
		const freeformEventItemId = Number(currentFreeformTarget.getAttribute('data-eventFreeformItemId'));
		tryToDeleteFreeformItem(freeformEventItemId, target);
	}
	else
	{
		// just hide the row
		console.log("just hide");
		currentFreeformTarget.className = "d-none";
	}

	
});

const tryToDeleteFreeformItem = async function(freeformEventItemId, target)
{
	
	
	let payload = new FormData;
	payload.append("freeformEventItemId", freeformEventItemId);
	
	
	const settings = {
	  method: 'POST',
	  body: payload,
  	};
  
  
  	
  	try
  	{
	  	const response = await fetch("./attemptDeleteFreeformEventItem", settings);
	  	const data = await response.json();
	  	handleDeleteFreeformItemResponse(data, target);
	  
  	}
	catch(err)
	{
		console.error(err);
	}
}

const handleDeleteFreeformItemResponse = function(data, target)
{
	console.log(data);
	
	// display response
	let messageContainer = document.getElementById("eventDetailsMessageContainer");
      
    // check to see if the DAO successful
    // otherwise, displays an error on the page
    if (data.errors.length == 0)
    {
		console.log("success");
        messageContainer.className = "alert alert-success eventDetailsMessageContainer";
        messageContainer.innerText = "Delete Successful!";
        target.className = "d-none";
        
    }
    else
    {
        messageContainer.innerText = data.errors[0];
      	messageContainer.className = "alert alert-danger eventDetailsMessageContainer";
        console.log("failure");
    }
    
	
	
}



const tryToSaveFreeformEventItems = function()
{
	let messageContainer = document.getElementById("eventDetailsMessageContainer");
	
	messageContainer.innerText = "";
	messageContainer.className = "alert alert-danger display-none eventDetailsMessageContainer";
	
	const eventFreeformItemTableRows = document.querySelectorAll(".eventFreeformItemTableRow");
	
	let errors = [];
	let items = [];
	for(let i = 0; i < eventFreeformItemTableRows.length; i++)
    {
            const currentTableRowElement = eventFreeformItemTableRows[i];
            const currentRowInputValue = currentTableRowElement.querySelector(".eventItemDataInput").value;

            // check if child input has value
            if(!currentRowInputValue)
            {
                errors.push("All Guest Provided Items must have a value");
				break;
            }
            
            // only add new items, all existing items are read only and cannot be changed
            if(!currentTableRowElement.className.includes("existingItem"))
            {
				 const newItem = {
                	name: currentRowInputValue,
                	userId: currentUserId,
            	};
            
            	items.push(newItem);
			}
                    

    }
    
    if(errors.length > 0)
    {
		// failure, display error
		messageContainer.className = "alert alert-danger eventDetailsMessageContainer";
		 messageContainer.innerText = errors[0];
        console.log("failure");
	}
	else
	{
		// continue, go to save items
		console.log("GO TO SAVE EVENT ITEMS");
		
		let payload = new FormData;
		payload.append("freeformItemListJson", JSON.stringify(items));
		payload.append("eventId", currentEventId);
        
        handleFreeformItemSave(payload);
	}
    
}

const handleFreeformItemSave = async function(data)
{
	const settings = {
	  method: 'POST',
	  body: data,
  	};
  
  
  	try
  	{
	  	const response = await fetch("./attemptSaveFreeformEventItems", settings);
	  	const data = await response.json();
	  	handleFreeformItemSaveResponse(data);
	  
  	}
	catch(err)
	{
		console.error(err);
	}
}

const handleFreeformItemSaveResponse = function(data)
{
	console.log(data);
	
	// display response
	let messageContainer = document.getElementById("eventDetailsMessageContainer");
      
    // check to see if the DAO successful
    // otherwise, displays an error on the page
    if (data.errors.length == 0)
    {
		console.log("success");
        messageContainer.className = "alert alert-success eventDetailsMessageContainer";
        messageContainer.innerText = "Save Successful!";

        
        // now force all freeform text items to be existingItems so they are ignored on next save
        // also disable all text box inputs & disable save button
        const eventFreeformItemTableRows = document.querySelectorAll(".eventFreeformItemTableRow");
	
	
		for(let i = 0; i < eventFreeformItemTableRows.length; i++)
    	{
            const currentTableRowElement = eventFreeformItemTableRows[i];
            
            // disable delete functionality for newly added freeform item
            if(!currentTableRowElement.className.includes("existingItem"))
            {
				let deleteFreeformItemX = currentTableRowElement.querySelector(".deleteFreeformItem");
				deleteFreeformItemX.dataset.toggle = "";
  				deleteFreeformItemX.dataset.target = "";
  				deleteFreeformItemX.innerHTML = "";
			}
            
            currentTableRowElement.className = "eventFreeformItemTableRow existingItem";
            
            const currentRowInput = currentTableRowElement.querySelector(".eventItemDataInput");     
            currentRowInput.readOnly = true;

    	}
    	

    	
    	let saveButton = document.getElementById("saveFreeformButton");
    	saveButton.disabled = true;
    }
    else
    {
		const errorMessages = data.errors.reduce((errorString, error) => {return errorString += error + "\n"}, "");
		
        messageContainer.innerText = errorMessages;
      	messageContainer.className = "alert alert-danger eventDetailsMessageContainer";
        console.log("failure");
    }
}