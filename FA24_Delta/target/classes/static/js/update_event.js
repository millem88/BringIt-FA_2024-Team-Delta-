// Authors: Abdinasir Aidrus (Nas)

const currentUserId = Number(document.getElementById("userIdHolder").getAttribute('data-userId'));
console.log(currentUserId);
let currentFreeformTarget = null;
let numOfEventItems = Number(document.getElementById("numOfEventItems").getAttribute('data-numOfEventItems'));



// Handels form inputs error checks and validation. (Nas)
// *Adapted from JM* 
const checkForValidEventCreationInputs = function()
{
    let response = {
        detailErrors: []
    };

    

    const eventId = document.getElementById("eventId").value;
    const eventNameInput = document.getElementById("name").value;
    const eventDescriptionInput = document.getElementById("description").value;
    const eventDateTimeInput = document.getElementById("dateTime").value;

    console.log(eventDescriptionInput);
    // console.log(eventDateTimeInput);

    if(eventNameInput === "")
    {
        response.detailErrors.push("Event Name cannot be empty");
    }
    if(!eventDescriptionInput)
    {
        response.detailErrors.push("Event Description cannot be empty");
    }
    if(!eventDateTimeInput)
    {
        response.detailErrors.push("Event Date/Time cannot be empty");
    }
    else
    {
        const date = new Date(eventDateTimeInput);
        const dateMilis = date.getTime();

        const currentMilies = Date.now();

        console.log("dateMilis ", dateMilis);
        console.log("currentMilies ", currentMilies);

        if(dateMilis < currentMilies)
        {
            response.detailErrors.push("Event Date/Time has to be in the future");
        }

    }

    return response;

}




const checkForValidFreeformEventItems = function()
{

	
	const eventFreeformItemTableRows = document.querySelectorAll(".eventFreeformItemTableRow");
	
	let response = {
        freeformErrors: [],
		freeformItems: [],
    };
	

	for(let i = 0; i < eventFreeformItemTableRows.length; i++)
    {
            const currentTableRowElement = eventFreeformItemTableRows[i];
            const currentRowInputValue = currentTableRowElement.querySelector(".eventItemDataInput").value;

            // check if child input has value
            if(!currentRowInputValue)
            {
                response.freeformErrors.push("All Guest Provided Items must have a value");
				break;
            }
            
            
            // only add new items, all existing items are read only and cannot be changed
            let currentRowUserId = Number(currentTableRowElement.getAttribute('data-assignedUserId'));
            
			const newItem = {
                name: currentRowInputValue,
                userId: currentRowUserId,
            };
            
            response.freeformItems.push(newItem);
			
                   

    }

    return response;
}



// Handels the submit event for the form. (Nas)
// *Adapted from JM* 
const checkForValidEventItems = function()
{
	let response = {
        eventItemErrors: [],
        eventItems: [],
    };
	
	const tableRowElements = document.querySelectorAll(".eventItemTableRow");

    console.log(tableRowElements);
    if(tableRowElements.length !== 0)
    {
        
        for(let i = 0; i < tableRowElements.length; i++)
        {
            const currentTableRowElement = tableRowElements[i];
            const currentRowInputValue = currentTableRowElement.querySelector(".eventItemDataInput").value;

            // check if child input has value
            if(!currentRowInputValue)
            {
                response.eventItemErrors.push("All Event Items must have a value");
                return response;
            }

            // continue to build items

            const checkboxElement = currentTableRowElement.querySelector(".eventItemCheckbox");
            let currentRowUserId = Number(currentTableRowElement.getAttribute('data-assignedUserId'));
            
            if(checkboxElement !== null)
            {
				const checkboxValue = checkboxElement.checked;
				
				currentRowUserId = checkboxValue ? currentUserId : -1;
			}
            // console.log(checkboxValue);

            const newItem = {
                name: currentRowInputValue,
                userId: currentRowUserId,
            }

            response.eventItems.push(newItem);
            

        }
    }
    
    return response;
}


document.getElementById("editEventSubmitButton").addEventListener("click", function(event)
{
	disableUpdateButtons();
	
    event.preventDefault();
    let messageContainer = document.getElementById("editEventMessageContainer");
	messageContainer.innerText = "";
	messageContainer.className = "alert alert-danger display-none createEventMessageContainer";
    
    let {detailErrors} = checkForValidEventCreationInputs();
    let {freeformErrors, freeformItems} = checkForValidFreeformEventItems();
    let {eventItemErrors, eventItems} = checkForValidEventItems();
    
    console.log("detailErrors", detailErrors);
    console.log("freeformErrors", freeformErrors);
    console.log("eventItemErrors", eventItemErrors);
    
    
    console.log("eventItems", eventItems);
    console.log("freeformItems", freeformItems);
    
    // combine all error arrays to have a total list of errors
    const errors = detailErrors.concat(freeformErrors, eventItemErrors);
    
    
    console.log(errors);
    
    if(errors.length === 0)
    {
		// generate form data, in addition append the JSON data for event items (both freeform and regular)
        let editEventFormElement = document.getElementById("editEventForm");
        let editEventFormData = new FormData(editEventFormElement);
        editEventFormData.append("freeformItemListJson", JSON.stringify(freeformItems));
        editEventFormData.append("eventItemListJson", JSON.stringify(eventItems));
     
        console.log("ALL GOOD");
        
        // go to update event
        handleEventUpdate(editEventFormData);
    }
    else
    {
        // invalid event, display errors

        const errorMessages = errors.reduce((errorString, error) => {return errorString += error + "\n"}, "");
        
        messageContainer.innerText = errorMessages;
		messageContainer.className = "alert alert-danger createEventMessageContainer";
    }
});

// this is used to disable buttons upon the user hitting submit
const disableUpdateButtons = function()
{
	document.getElementById("editEventSubmitButton").disabled = true;
	document.getElementById("addEventItemBtn").disabled = true;
	document.getElementById("editEventDeleteButtom").disabled = true;
	
}

const disableDeleteItemXs = function()
{
	
	const eventFreeformItemTableRows = document.querySelectorAll(".eventFreeformItemTableRow");
	for(let i = 0; i < eventFreeformItemTableRows.length; i++)
    {
       const currentTableRowElement = eventFreeformItemTableRows[i];
            
       // disable delete functionality for newly added freeform item

		let deleteFreeformItemX = currentTableRowElement.querySelector(".deleteFreeformItem");
		deleteFreeformItemX.dataset.toggle = "";
  		deleteFreeformItemX.dataset.target = "";
  		deleteFreeformItemX.innerHTML = "";

    }
    
    
    
    const eventItemTableRows = document.querySelectorAll(".eventItemTableRow");
    for(let i = 0; i < eventItemTableRows.length; i++)
    {
       const currentTableRowElement = eventItemTableRows[i];
            
       // disable delete functionality for newly added freeform item
       
		let deleteItemX = currentTableRowElement.querySelector(".deleteTableRow");
		deleteItemX.dataset.toggle = "";
  		deleteItemX.dataset.target = "";
  		deleteItemX.innerHTML = "";
		
            

    }
    
}


// this is used to enable the buttons after a response has come back after trying to update an event
const enableButtons = function()
{
	document.getElementById("editEventSubmitButton").disabled = false;
	document.getElementById("addEventItemBtn").disabled = false;
}


// Handles a post request to  "./updateEvent" when the form is submitted and update event details. (Nas)
// *Adapted from JM* 

const handleEventUpdate = function(formData)
{


  // fetch to endpoint via a POST 
  fetch
  (
    "./updateEvent",
    {
    method: "POST",
    body: formData
    }
  )
  .then(response => 
  {
    response.json().then(controllerResponse => 
    { 
	
	  let messageContainer = document.getElementById("editEventMessageContainer");
	  messageContainer.innerText = "";
	  messageContainer.className = "alert alert-danger display-none createEventMessageContainer";
		
		
	  enableButtons();
		
	  // gets controller response object off the response from the controller
      console.log(controllerResponse);
      
      // check to see if event update was successful
      if(controllerResponse.errors.length === 0)
      {
		// success
		messageContainer.innerText = "Event Update Successful!";
		messageContainer.className = "alert alert-success createEventMessageContainer";
		

      }
      else
      {
        // failure to update event
        const errorMessages = controllerResponse.errors.reduce((errorString, error) => {return errorString += error + "\n"}, "");

        messageContainer.innerText = errorMessages;
		messageContainer.className = "alert alert-danger createEventMessageContainer";
      }
    })
  });

}

// event listener to handle deleting an freeform event item
document.getElementById("eventFreeformItemTable").addEventListener("click", function(event)
{
    // event.preventDefault();
    
    
    const eventTargetClassName = event.target.className;
    if(eventTargetClassName.includes("deleteFreeformItem"))
    {
        const closestRow = event.target.closest("tr");
        console.log(closestRow);
        //closestRow.className = "d-none";
        
        
        currentFreeformTarget = closestRow;
        
        

    }

    
});


// event listener to handle deleting an normal event item
document.getElementById("eventItemTable").addEventListener("click", function(event)
{
    // event.preventDefault();
    
    
    const eventTargetClassName = event.target.className;
    if(eventTargetClassName.includes("deleteTableRow"))
    {
        const closestRow = event.target.closest("tr");
        console.log(closestRow);
        //closestRow.className = "d-none";
        
        
        currentFreeformTarget = closestRow;
        
        

    }

    
});



// hide the currently deleted event item
document.getElementById("event-item-delete").addEventListener("click", function(event)
{
	$('#deleteItemModal').modal('hide')
	
	
	currentFreeformTarget.className = "d-none";
	
});


// add new event item handler
document.getElementById("addEventItemBtn").addEventListener("click", function(event)
{
    event.preventDefault();
    
    let tableEle = document.getElementById("eventItemTable");
    
    const newTableRowHTML = `
        <tr class="eventItemTableRow">
                            <td class="eventItemTableData deleteTableRow" data-toggle="modal" data-target="#deleteItemModal">
                                ❌
                            </td>
                
                            <td class="eventItemTableData">
                                <label for="eventItemInput" class="eventItemLable">${++numOfEventItems}</label> 
                            </td>

                            <td class="eventItemTableData">
                                <input type="text" class="eventItemDataInput" name="eventItemInput">
                            </td>

                            <td class="eventItemTableData" >
                                <input type="checkbox" class="eventItemCheckbox" name="eventItemCheckbox">
                            </td>
                        </tr>
    `;


    tableEle.insertAdjacentHTML("beforeend", newTableRowHTML);
    document.getElementById("eventItemTable").className = "eventItemTable";
});



// delete event listener
document.getElementById("event-delete").addEventListener("click", function(event)
{
	disableDeleteItemXs();
	disableUpdateButtons();
	$('#deleteEventModal').modal('hide')
	
	// TODO: POST to delete event
	let editEventFormElement = document.getElementById("editEventForm");
    let editEventFormData = new FormData(editEventFormElement);
    
    handleDeleteEvent(editEventFormData);
});

const handleDeleteEvent = function(formData)
{
  // fetch to endpoint via a POST 
  fetch
  (
    "./attemptDeleteEvent",
    {
    method: "POST",
    body: formData
    }
  )
  .then(response => 
  {
    response.json().then(controllerResponse => 
    { 
	  let messageContainer = document.getElementById("editEventMessageContainer");
	  messageContainer.innerText = "";
	  messageContainer.className = "alert alert-danger display-none createEventMessageContainer";

		
	  // gets controller response object off the response from the controller
      console.log(controllerResponse);
      
      // check to see if event update was successful
      if(controllerResponse.errors.length === 0)
      {
		// success
		messageContainer.innerText = "Delete Event Successful!";
		messageContainer.className = "alert alert-success createEventMessageContainer";
		

      }
      else
      {
        // failure to update event
        const errorMessages = controllerResponse.errors.reduce((errorString, error) => {return errorString += error + "\n"}, "");

        messageContainer.innerText = errorMessages;
		messageContainer.className = "alert alert-danger createEventMessageContainer";
      }
    })
  });
}