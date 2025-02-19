<!--
	Login JSP file, allows users to log into the application
	 -- this will also be the default landing page of non-logged in users once the authentication filter is created.
	Author(s): Jamie Mizelle
	Date: 9/29/24
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c"    uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    
    <link rel="icon" href="images/favicon.png" type="image/x-icon"> <!-- LG 10/16/24 Added favicon -->

    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <!-- local css/js files-->
    <link rel="stylesheet" href="css/style.css">
    
</head>
<body>
    <div class="loginContainer">

        <div class="loginContent">
            
            <div class="loginLogoContainer ">
                <img src="images/wordMark.png" alt="" class="loginLogo"> <!--  LG 10/16/24 changed from lock.png to wordMark.png -->
            </div>
            
            <div class="loginFormContainer">
                <form action="" id="loginForm" class="loginForm">

                    <div class="loginInputContainer">
                        <div class="loginEmailContainer">
                            <label for="email" class="loginLabel">Email:</label>
                            <input type="text" id="emailInput" name="email" class="loginInput" required>
                        </div>
                        
                        <div class="loginPasswordContainer">
                            <label for="password" class="loginLabel">Password:</label>
                            <input type="password" id="passwordInput" name="password" class="loginInput" required>
                        </div>
                    </div>

                    
                    
                    <div class="loginShowPasswordContainer">
                        <input type="checkbox" class="loginShowPassword" onclick="togglePasswordVisability()">
                        <label for="password" class="loginCheckboxLabel">Show Password</label>
                    </div>

                    <div class="loginSubmitFormButtonContainer">
                        <input type="submit" value="Login" class="loginSubmitButton" id="loginSubmitButton">
                    </div>
                    
                    <c:if test ="${logoutMessage == null}">
                    
                    	<div class="alert alert-danger display-none loginMessageContainer" role="alert" id="loginMessageContainer">
  						
						</div>

					</c:if>
					
					<c:if test="${logoutMessage != null}">
						
						
         				<div class="alert alert-success loginMessageContainer" role="alert" id="loginMessageContainer">
         				${logoutMessage}
         				</div>
         				
       				</c:if>
					
                    <div class="loginRegisterLinkContainer">
                        <p class="loginLinkPara">
                            Don't have an account? Sign up <a href="./register">here</a>!
                        </p>
                    </div>
                    
                    

                </form>
            </div>
            

        </div>

    </div>

    <!-- Optional JavaScript -->
    <!-- Popper.js, then Bootstrap JS -->
    <script src="js/login.js"></script>
   
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>