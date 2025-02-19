// JS for create_event.jsp, handles all frontend functionality for creating an event
// this includes validating event details and event items
// Author(s): Jamie Mizelle
// Date: 11/13/2024

let numOfCreatedItems = 0;
let currentNumOfItems = 0;

const currentUserId = Number(document.getElementById("userIdHolder").getAttribute('data-userId'));
console.log(currentUserId);




document.getElementById("addEventItemBtn").addEventListener("click", function(event)
{
    event.preventDefault();
    
    let tableEle = document.getElementById("eventItemTable");
    
    const newTableRowHTML = `
        <tr class="eventItemTableRow">
                            <td class="eventItemTableData deleteTableRow">
                                ❌
                            </td>
                
                            <td class="eventItemTableData">
                                <label for="eventItemInput" class="eventItemLable">${++numOfCreatedItems}</label> 
                            </td>

                            <td class="eventItemTableData">
                                <input type="text" class="eventItemDataInput" name="eventItemInput">
                            </td>

                            <td class="eventItemTableData" >
                                <input type="checkbox" class="eventItemCheckbox" name="eventItemCheckbox">
                            </td>
                        </tr>
    `;
    currentNumOfItems++;

    tableEle.insertAdjacentHTML("beforeend", newTableRowHTML);
    document.getElementById("eventItemTable").className = "eventItemTable";
});



document.getElementById("eventItemTable").addEventListener("click", function(event)
{
    // event.preventDefault();
    
    
    const eventTargetClassName = event.target.className;
    if(eventTargetClassName.includes("deleteTableRow"))
    {
        const closestRow = event.target.closest(".eventItemTableRow");
        console.log(closestRow);
        closestRow.className = "d-none";
        currentNumOfItems--;

        if(currentNumOfItems == 0)
        {
            document.getElementById("eventItemTable").className = "d-none";
        }
    }

    
});

document.getElementById("createEventSubmitButton").addEventListener("click", function(event)
{
    event.preventDefault();
    let messageContainer = document.getElementById("createEventMessageContainer");
	messageContainer.innerText = "";
	messageContainer.className = "alert alert-danger display-none createEventMessageContainer";
    
    const {errors, items} = checkForValidEventCreationInputs();
    console.log(errors);
    
    if(errors.length === 0)
    {
        // valid event, create FormData and append all items to it under "itemList" 
        console.log("items = ", items);

        let createEventFormElement = document.getElementById("createEventForm");
        let createEventFormData = new FormData(createEventFormElement);
        
        //items.forEach((item) => 
        //{
            //createEventFormData.append('itemList', item);
        //});
        
        
		createEventFormData.append("itemListJson", JSON.stringify(items));
        
        handleEventCreationSubmission(createEventFormData);
    }
    else
    {
        // invalid event, display errors

        const errorMessages = errors.reduce((errorString, error) => {return errorString += error + "\n"}, "");
        
        messageContainer.innerText = errorMessages;
		messageContainer.className = "alert alert-danger createEventMessageContainer";
    }
});


const checkForValidEventCreationInputs = function()
{
    let response = {
        errors: [],
        items: [],
    };

    

    const eventId = document.getElementById("eventId").value;
    const eventNameInput = document.getElementById("name").value;
    const eventDescriptionInput = document.getElementById("description").value;
    const eventDateTimeInput = document.getElementById("dateTime").value;

    console.log(eventDescriptionInput);
    // console.log(eventDateTimeInput);

    if(eventNameInput === "")
    {
        response.errors.push("Event Name cannot be empty");
    }
    if(!eventDescriptionInput)
    {
        response.errors.push("Event Description cannot be empty");
    }
    if(!eventDateTimeInput)
    {
        response.errors.push("Event Date/Time cannot be empty");
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
            response.errors.push("Event Date/Time has to be in the future");
        }

    }

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
                response.errors.push("All Event Items must have a value");
                return response;
            }

            // continue to build items

            const checkboxValue = currentTableRowElement.querySelector(".eventItemCheckbox").checked;
            // console.log(checkboxValue);

            const newItem = {
                name: currentRowInputValue,
                userId: checkboxValue ? currentUserId : -1,
            }

            response.items.push(newItem);
            

        }
    }

    return response;

}



const handleEventCreationSubmission = function(formData)
{
  // fetch to endpoint via a POST 
  fetch
  (
    "./eventCreationAttempt",
    {
    method: "POST",
    body: formData
    }
  )
  .then(response => 
  {
    response.json().then(controllerResponse => 
    { 
	  let messageContainer = document.getElementById("createEventMessageContainer");
	  messageContainer.innerText = "";
	  messageContainer.className = "alert alert-danger display-none createEventMessageContainer";
		
		
	  // gets controller response object off the response from the controller
      console.log(controllerResponse);
      
      // check to see if event creation was successful
      if(controllerResponse.errors.length === 0)
      {
		// success
		messageContainer.innerText = "Event Creation Successful!";
		messageContainer.className = "alert alert-success createEventMessageContainer";
		
		// disable submit button after succesful event creation
		document.getElementById("createEventSubmitButton").disabled = true;
      }
      else
      {
        // failure to create event
        const errorMessages = controllerResponse.errors.reduce((errorString, error) => {return errorString += error + "\n"}, "");

        messageContainer.innerText = errorMessages;
		messageContainer.className = "alert alert-danger createEventMessageContainer";
      }
    })
  });
}