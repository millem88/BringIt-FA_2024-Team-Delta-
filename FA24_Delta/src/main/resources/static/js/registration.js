// JS file for register.jsp, handles all frontend functionality for user registration.
// Author(s): Jamie Mizelle
// Date: 11/13/2024

const emailRegexPattern = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;

const passwordRegexPattern = /^[a-zA-Z0-9_]*$/;


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


document.getElementById("registrationButton").addEventListener("click", function(event)
{
    event.preventDefault();
    console.log("submit clicked");

    let messageContainer = document.getElementById("registrationMessageContainer");
	messageContainer.innerText = "";
	messageContainer.className = "alert alert-danger display-none registrationMessageContainer";

    let errors = [];

    const firstNameInput = document.getElementById("firstNameInput").value;
    const lastNameInput = document.getElementById("lastNameInput").value;
    const emailInput = document.getElementById("emailInput").value;
    const passwordInput = document.getElementById("passwordInput").value;

    // validate all fields
    if(firstNameInput.length > 32 || !firstNameInput.length)
    {
        errors.push("First Name cannot be empty and must be <= 32 characters");
    }

    if(lastNameInput.length > 32 || lastNameInput.length <= 0)
    {
        errors.push("Last Name cannot be empty and must be <= 32 characters");
    }

    if(!emailRegexPattern.test(emailInput))
    {
        console.log("BAD EMAIL");
        errors.push("Email is not in valid format");
    }

    if(emailInput.length > 256)
    {
        errors.push("Email must be less than 256 characters");
    }

    if(!passwordRegexPattern.test(passwordInput))
    { 
        errors.push("Password can only contain characters, numbers, and _");
    }

    if(passwordInput.length < 8 || passwordInput.length > 16)
    {
        errors.push("Password must be between 8 and 16 characters");
    }




    console.log(firstNameInput);
    console.log(lastNameInput);
    console.log(emailInput);
    console.log(passwordInput);


    if(errors.length === 0)
    {
        let registrationFromElement = document.getElementById("registrationForm");
        let registrationFormData = new FormData(registrationFromElement);

        console.log(registrationFormData);
        // VALID REGISTRATION ATTEMPT
        handleRegistrationAttempt(registrationFormData);
    }
    else
    {
        // INVALID REGISTRATION ATTEMPT
        const errorMessages = errors.reduce((errorString, error) => {return errorString += error + "\n"}, "");

        messageContainer.innerText = errorMessages;
		messageContainer.className = "alert alert-danger registrationMessageContainer";
    }


    

    
});


function handleRegistrationAttempt(registrationFormData)
{
    // fetch to endpoint via a POST 
  fetch
  (
    "./registrationAttempt",
    {
    method: "POST",
    body: registrationFormData
    }
  )
  .then(response => 
  {
    response.json().then(controllerResponse => 
    { 
	  // gets controller response object off the response from the controller
      console.log(controllerResponse);
      
      // get pointer to message container element
      let messageContainer = document.getElementById("registrationMessageContainer");
      
      // check to see if the DAO was able to get a real user from the inputs, registration was successful
      // otherwise, displays an error on the page
      if (controllerResponse.user.id == -1)
      {
        messageContainer.innerText = "Error, registration failed or account with supplied email already exists.";
        messageContainer.className = "alert alert-danger registrationMessageContainer";
        console.log("failure");
      }
      else
      {
          console.log("success");
          messageContainer.className = "alert alert-success registrationMessageContainer";
          messageContainer.innerText = "Registration Successful!";
      }
    })
  });
}