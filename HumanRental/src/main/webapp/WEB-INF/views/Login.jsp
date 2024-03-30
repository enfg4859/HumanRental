<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>    
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <title>Document</title>
	    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
	    <script src="https://kit.fontawesome.com/c5a6a42a0b.js" crossorigin="anonymous"></script>
	
	    <link rel="stylesheet" href="<c:url value="/resources/css/style_nav.css"/>"> 
	    <link rel="stylesheet" href="<c:url value="/resources/css/style_section1.css"/>">
	    <link rel="stylesheet" href="<c:url value="/resources/css/style_section2.css"/>">
	    <link rel="stylesheet" href="<c:url value="/resources/css/style_section3.css"/>">
	    <link rel="stylesheet" href="<c:url value="/resources/css/style_footer.css"/>">
		
		
		<!-- jquery -->		
	    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		
		
		<!-- js -->	    
   	    <script src="<c:url value="/resources/js/login.js" />"></script>
	</head>
	<body class="vh-100">
	    <jsp:include page="nav.jsp" />
	    <div style="height: 80%;" class="container justify-content-center text-center d-flex flex-column align-items-center">
	    	<div class="row"><h1>로그인</h1></div>
		    <div class="row justify-content-center" >
			    <form:form modelAttribute="member" method="post" class="row justify-content-center">
			    	<div class="row justify-content-center">
			    		<div class="col-2 p-3 align-self-center">아이디</div>
			    		<div class="col-3 p-3 "><form:input path="memberId" name="username" id="memberId" class="form-control"/></div>
		    		</div>
			    	<div class="row justify-content-center">
			    		<div class="col-2 p-3 align-self-center">비밀번호</div>
			    		<div class="col-3 p-3"><form:input type="password" path="memberPw" name="password" id="memberPw" class="form-control"/></div>
			    	</div>
			    	<div class="row">
			    		<div><input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }"></div>
				    	<div class="col p-3 justify-content-center">
				    		<a onclick="loginCheck()" class="col-sm-2 btn btn-outline-primary">로그인</a>
				    	</div>
			    	</div>
			    	<div class="row justify-content-center">
<!-- 			    		<div class="row p-3 justify-content-center">
			    			<a href="#" class="col-sm-2 btn btn-outline-warning ">카카오 로그인</a>
		    			</div>
				    	<div class="row p-3 justify-content-center">
				    		<a href="#" class="col-sm-2 btn btn-outline-success">네이버 로그인</a>
			    		</div> -->
				    	<div class="row p-3 justify-content-center">
				    		<a href="<c:url value="/join" />" class="col-sm-2 btn btn-dark">회원 가입</a>
			    		</div>
			    	</div> 
			    </form:form>
		    </div>
	    </div>
	</body>
</html>