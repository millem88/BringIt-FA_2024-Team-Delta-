/// Login page JS file, used to handle all the frontend functionality for the login page
/// Author(s): Jamie Mizelle
/// Date: 9/29/24

// function to toggle password input box text visability based on the status of the "Show Password" checkbox
let togglePasswordVisability = function()
{
    // get passwordInputElement
    let passwordInputElement = document.getElementById("passwordInput");

    // toggle passwordInputElement input type
    if (passwordInputElement.type === "password") 
    {
        passwordInputElement.type = "text";
    } 
    else 
    {
        passwordInputElement.type = "password";
    }
}

// event/click listener on the login button
document.getElementById("loginSubmitButton").addEventListener("click", function(event)
{
	console.log("submit clicked");
	// prevent form automatically submission
	event.preventDefault();
	
	
	// get message container element and reset content/display
	let messageContainer = document.getElementById("loginMessageContainer");
	messageContainer.innerText = "";
	messageContainer.className = "alert alert-danger display-none loginMessageContainer";
	
	
    
    // check to see if the email or password inputs are null, if they are display error and cancel submission
    let passwordInputValue = document.getElementById("passwordInput").value;
    let emailInputValue = document.getElementById("emailInput").value;
    
    if(emailInputValue === "" || passwordInputValue === "")
    {
		messageContainer.innerText = "Please input both an email and password!";
		messageContainer.className = "alert alert-danger loginMessageContainer";
	}
	else
	{
		// valid email and password inputs, create form data object and pass it to handleLoginAttempt
		let loginFormElement = document.getElementById("loginForm");
    	let loginFormData = new FormData(loginFormElement);
	
    	console.log(loginFormData);

    	handleLoginAttempt(loginFormData);
	}
    

    
});

// handle login attempt function, sends data to loginAttempt controller endpoint and tries to log the user into the application
let handleLoginAttempt = function(loginFormData)
{
  // fetch to endpoint via a POST 
  fetch
  (
    "./loginAttempt",
    {
    method: "POST",
    body: loginFormData
    }
  )
  .then(response => 
  {
    response.json().then(controllerResponse => 
    { 
	  // gets controller response object off the response from the controller
      console.log(controllerResponse);
      
      // get pointer to message container element
      let messageContainer = document.getElementById("loginMessageContainer");
      
      // check to see if the DAO was able to get a real user from the inputs, if successful, moves to index page (will need to be changed later)
      // otherwise, displays an error on the page
      if (controllerResponse.user.id == -1)
      {
        messageContainer.innerText = "Error, account not found!";
        messageContainer.className = "alert alert-danger loginMessageContainer";
        console.log("login failure");
      }
      else
      {
          console.log("login success");
          window.location.replace("./myEvents");
      }
    })
  });
}