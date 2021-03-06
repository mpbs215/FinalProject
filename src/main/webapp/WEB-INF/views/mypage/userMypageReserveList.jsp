<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('.updateR').click(function(){
			window.open("${pageContext.request.contextPath}/user/updateReserveForm?parkNo="+$(this).attr("id")+"&reserveNo="+$(this).attr("name"), "변경하기", "width=1000, height=600, left=100, top=50");
		})
	})
</script>
</head>
<body>
<div class="container rounded" style="background-color: rgba( 0, 0, 0, .7 );">
  <h2 class="text-white font-weight-bold">예약 현황</h2>
  <p class="text-white">현재 예약 현황 조회 및 수정 취소를 할 수 있습니다.</p>
  <div class="row">
  	<c:forEach items="${list}" var="dto">
	 <div class="col-sm-4" style="margin-bottom: 5%">
	  <div class="card">
	    <img class="card-img-top border" src="${pageContext.request.contextPath}/resources/images/park/${dto.parkImg.imgPath}" alt="Card image" style="width:100%; height: 30%">
	    <div class="card-body">
	      <h4 class="card-title text-center font-weight-bold"><a href="${pageContext.request.contextPath}/user/userReserveForm?parkNo=${dto.parkNo}">${dto.parkName}</a></h4>
	      <p class="card-text text-center">
	      	예약일 : ${dto.parkReserve.reserveStart}<br />
	      	판매자 : ${dto.userId}<br />
	      	연락처 : ${dto.user.hp}<br />
	      	총가격 : ${dto.price}<br />
	      </p>
	      <div class="row">
		      <div class="col-sm-6 justify-content-center text-center">
		      	<a href="${pageContext.request.contextPath}/user/deleteReserve?reserveNo=${dto.parkReserve.reserveNo}" class="btn btn-danger font-weight-bold">예약취소</a>
		      </div>
		      <div class="col-sm-6 justify-content-center text-center">
		      	<button class="btn btn-success font-weight-bold updateR" name="${dto.parkReserve.reserveNo}" id="${dto.parkNo}">예약변경</button>
		      </div>
	      </div>
	    </div>
	  </div>
	</div>
	</c:forEach>
  </div>
</div>
  <br>
</body>
</html>