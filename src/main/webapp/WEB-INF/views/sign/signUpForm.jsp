<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
 <script src="http://code.jquery.com/jquery-latest.min.js"></script>
 
 <style>
	span{color:white; width:100px; }
	div{color:white;}
	.signUp-Form { 
		display:inline-block; 
		text-align:center; 
		width:400px; 
		margin-left:33%;
		background:rgba(0,0,0,0.5);
	}
 </style>
 
<script type="text/javascript">
	// 주소 검색 팝업창 띄우기
	function goPopup() {
		// 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(http://www.juso.go.kr/addrlink/addrCoordUrl.do)를 호출하게 됩니다.
		var pop = window
				.open(
						"${pageContext.request.contextPath}/addrPopup?${_csrf.parameterName}=${_csrf.token}",
						"pop",
						"width=570,height=420, scrollbars=yes, resizable=yes");
	}
	
	// 주소 입력
	function jusoCallBack(roadFullAddr, roadAddrPart1, addrDetail,
			roadAddrPart2, engAddr, jibunAddr, zipNo, admCd, rnMgtSn, bdMgtSn,
			detBdNmList, bdNm, bdKdcd, siNm, sggNm, emdNm, liNm, rn, udrtYn,
			buldMnnm, buldSlno, mtYn, lnbrMnnm, lnbrSlno, emdNo, entX, entY,
			parameterName, token) {
		// 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
		var addr=document.getElementById("address");
		addr.value = roadFullAddr;
	}

$(document).ready(function(){
	
	var checkResultId="";		
	$("#signUpForm").submit(function(){			
		if($("#signUpForm :input[name=userId]").val().trim()==""){
			alert("아이디를 입력하시지 않았습니다. 아이디를 입력해주세요");				
			return false;
		}
		if($("#signUpForm :input[name=password]").val().trim()==""){
			alert("패스워드를 입력하세요");				
			return false;
		}
		if($("#signUpForm :input[name=userName]").val().trim()==""){
			alert("이름을 입력하세요");				
			return false;
		}
		
		if($("#signUpForm :input[name=email]").val().trim()==""){
			alert("이메일 주소를 입력하세요");				
			return false;
		}	
		
		if($("#signUpForm :input[name=hp]").val().trim()==""){
			alert("핸드폰 번호를 입력하세요");				
			return false;
		}	
		
		if($("#signUpForm :input[name=address]").val().trim()==""){
			alert("주소를 입력하세요");				
			return false;
		}	
		
		if(checkResultId==""){
			alert("아이디 중복확인을 하세요");
			return false;
		}		
	});//submit
	
	$("#signUpForm :input[name=userId]").keyup(function(){
		var id=$(this).val().trim();
		if(id.length <  4 || id.length > 10){
			$("#idCheckView").html("입력 가능한 아이디의 범위는 4~10 글자 입니다.").css("background","pink");
			checkResultId="";
		} else {
			
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/sign/idCheck",				
			data:"${_csrf.parameterName}=${_csrf.token}&userId="+id,	
			dataType : "text",
			success:function(message){		
				console.log(message);
				if(message == "fail"){
				$("#idCheckView").html("  "+id+" ID Can't Use!! ").css("background","orange");
					checkResultId="";
				}else{						
					$("#idCheckView").text("  "+id+" ID Can Use!! ").css("background","yellow");		
					checkResultId=userId;
					}					
				}//callback			
			});//ajax
		}
	});//keyup
});//ready
</script>
</head>

<body>
<div class="signUp-Form" name="signUp-Form">
<h2>User SignUp Form</h2><p>
	<form method="post" action="${pageContext.request.contextPath}/sign/signUp" id="signUpForm">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		<div>ID </div><input type="text" name="userId" id="userId"><div id="idCheckView" >아이디 중복 확인</div><br><br>
		<div>PASSWORD </div><div><input type="password" name="password"><br><br></div>
		<div>NAME </div><div><input type="text" name="userName"><br><br></div>
		<div>E-Mail</div><div><input type="text" name="email"><br><br></div>
		<div>HP : </div><div><input type="text" name="hp"><br><br></div>
		<div>주차장 등록을 하기 위해서는 핸드폰 본인 인증을 하셔야 합니다.</div>
		<div>ADDRESS : </div><div><input type="text" name="address" id="address" onclick="goPopup();"/><br><br></div>
		 <input type="hidden" name="regidate"><br>
		<div>User_Type</div><input type="hidden" value="0" name="seller"><p>
		<input type="submit" value="회원가입하기">
	</form>
</div>
</body>
</html>