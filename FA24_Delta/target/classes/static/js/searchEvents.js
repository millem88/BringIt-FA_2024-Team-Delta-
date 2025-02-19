// Authors: Abdinasir Aidrus (Nas)

// Row container for the events (Nas)
const joinableEventsElement = document.getElementById("joinable-events");

let globalEvents = [];





const fetchEvents = async () => {

  // settings for fetch
  const settings = {
	  method: 'POST',
  };
  
  
  // fetch to the "./fetchNotCreatedEvents" controller endpoint instead of the dummy data.
  try
  {
	  const response = await fetch("./fetchNotCreatedEvents", settings);
	  const data = await response.json();
	  insertEventData(data);
	  
  }
  catch(err)
  {
	  console.error(err);
  }
}

 


const insertEventData = function(data)
{
	console.log(data);
	
	const {events} = data;
	
	console.log(events);
	
	globalEvents = events;
	
	let tempEvent = "";

  	events.forEach((event) => {

    const {id, name, description, dateTime } = event;
    
    const date = new Date(dateTime);
    
    const dateString = date.toDateString();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const dateTimeFormatted = date.toLocaleTimeString('en-US', {
  		hour: 'numeric',
  		minute: '2-digit',
  		hour12: true
	});


    //const [date, time] = dateTime;


    tempEvent += `
        <!-- Column -->
		
        <div class="col-12 col-md-6 col-lg-4 my-3 d-flex justify-content-center">

         <!-- Bootstrap card for holding an event (Nas)-->
		 
            <div class="card eventCard" style ="width:20rem;" data-event_id ="${id}">
            <div class="card-header event-card-header">
                <strong style="color: white;">${name}</strong> 
            </div>
            <div class="card-body">
                <p class="card-text">
                <strong>Description:</strong> ${description}
                </p>
            </div>
            <div class="card-footer d-flex justify-content-between">
                <p class="card-text">
                    <strong>Date:</strong> ${dateString}
                </p>
                <p class="card-text">
                    <strong>Time:</strong> ${dateTimeFormatted}
                </p>
            </div>
            </div>

      </div>
        
        `;
  })

  joinableEventsElement.insertAdjacentHTML("beforeend", tempEvent);
}


joinableEventsElement.addEventListener("click", function(event)
{
	console.log("clicked");
	
	//const eventTargetClassName = event.target.className;
	const closestEventCard = event.target.closest(".eventCard");
	
    if(closestEventCard)
    {
		console.log("card clicked");
		
		//const closestEventCard = event.target.closest(".eventCard");
		
		const eventId = Number(closestEventCard.getAttribute('data-event_id'));
		
		const redirectURL = `./eventClicked?eventId=${eventId}`;
		
		window.location.replace(redirectURL);
		
		console.log("eventId = ", eventId);
	}
});


fetchEvents();