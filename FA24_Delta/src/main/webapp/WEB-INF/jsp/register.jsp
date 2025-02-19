
<!--
	Register JSP file, used to let users register for 'BringIt!' application.
	Author(s): Jamie Mizelle
	Date: 11/13/2024
 -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration</title>

	<link rel="icon" href="images/favicon.png" type="image/x-icon"> <!-- LG 10/16/24 Added favicon -->
	
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <!-- local css/js files-->
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="registrationContainer">

        <div class="registrationContent">

            <div class="registrationFormContainer">

                <div class="registrationIconContainer">
                
                    <img src="images/person.png" alt="" class="registrationIcon">
                </div>

                <h1 class="registrationTitle">
                    Register Account
                </h1>

                <form id="registrationForm" class="registrationForm">

                    <div class="registrationFirstName">
                        <label for="firstName" class="registrationLabel">First Name</label>
                        <input type="text" name="firstName" class="registrationInput" id="firstNameInput">
                    </div>
                    

                    <div class="registrationLastName">
                        <label for="lastName" class="registrationLabel">Last Name</label>
                        <input type="text" name="lastName" class="registrationInput" id="lastNameInput">
                    </div>


                    <div class="registrationEmail">
                        <label for="email" class="registrationLabel">
                            Email
                        </label>
                        <input type="email" name="email" class="registrationInput" id="emailInput">
                    </div>

                    <div class="registrationPassword">
                        <label for="password" class="registrationLabel">Password</label>
                        <input type="password" name="password" id="passwordInput" class="registrationInput">
                    </div>

                    <div class="registrationPasswordContainer">
                        <input type="checkbox" class="loginShowPassword" onclick="togglePasswordVisability()">
                        <label for="password" class="loginCheckboxLabel">Show Password</label>
                    </div>

                    <div class="registrationSubmitContainer">
                        <input type="submit" value="Register" class="registrationSubmitButton" id="registrationButton"> 
                    </div>
                </form>

                <div class="alert alert-danger display-none registrationMessageContainer" role="alert" id="registrationMessageContainer">
                        
                </div>

                <div class="loginRegisterLinkContainer">
                    <p class="loginLinkPara">
                        Already have an account? login <a href="./login">here</a>!
                    </p>
                </div>
                

            </div>

        </div>

    </div>
    
</body>

    <!-- Optional JavaScript -->
    <!-- Popper.js, then Bootstrap JS -->
    <script src="js/registration.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</html>