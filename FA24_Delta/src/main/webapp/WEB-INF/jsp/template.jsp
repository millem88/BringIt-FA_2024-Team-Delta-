<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix= "c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>

<head>
  <%@include file="commonHeadContent.jsp" %>
  <title>Template</title>
</head>

<body>
	<%@ include file="/WEB-INF/jsp/navbar.jsp" %> 
	<!-- unique page content begin -->
	<p>Hello ${name}</p>
	
	<ul>
		<c:forEach var="string" items="${stringList}">
        	<li>${string}</li>
   		</c:forEach>
	</ul>
	
	<c:if test = "${conditionalValue.equals('')}">
       <p>I don't exist!</p>
    </c:if>
    
    <c:if test = "${!conditionalValue.equals('')}">
       <p>conditionalValue = ${conditionalValue}</p>
    </c:if>
    <!-- unique page content end -->
</body>


	<%@include file="commonPostBody.jsp" %>
	
</html>