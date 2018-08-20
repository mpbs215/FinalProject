<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="//mugifly.github.io/jquery-simple-datetimepicker/jquery.simple-dtpicker.js"></script>
<link rel="stylesheet" href="//mugifly.github.io/jquery-simple-datetimepicker/jquery.simple-dtpicker.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css">
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=5a1af573ee6ab29bacc2369936a8b908&libraries=services"></script>
<script>
	$(function(){
		var markers = [];
		var infowindows = [];
		var container = document.getElementById('map');
		var options = {
			center: new daum.maps.LatLng(33.450701, 126.570667),
			level: 3
		};
		

		var map = new daum.maps.Map(container, options);
		
		// 주소-좌표 변환 객체를 생성합니다
		var geocoder = new daum.maps.services.Geocoder();
		
		$("input[name='reserveDate']").appendDtpicker(
				{locale : 'ko',
				 autodateOnStart : false,
				 todayButton: true,
				 futureOnly: true,
				}
		);
		$("#sido").change(function(){
			$("[name='parkAddr']").val($("#sido").val())
			$("#gugun").html="";
			$("#dong").html="";
			$("#ri").html="";
			var sido=$("#sido").val();
			var options="<option value=''></option>";
			$.ajax({
				url:"${pageContext.request.contextPath}/user/selectGugun",
				data:"sido="+sido,
				dataType:"json",
				success: function(result){
 					$.each(result, function(index, item){
						options+="<option value='"+item+"'>"+item+"</option>"
					})
					$("#gugun").html(options);
				},
				error:function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}	
			})
		})
		
		$("#gugun").change(function(){
			$("[name='parkAddr']").val($("#gugun").val())
			$("#dong").html="";
			$("#ri").html="";
			var gugun=$("#gugun").val();
			var options="<option value=''></option>";
			$.ajax({
				url:"${pageContext.request.contextPath}/user/selectDong",
				data:"gugun="+gugun,
				dataType:"json",
				success: function(result){
					$.each(result, function(index, item){
						options+="<option value='"+item+"'>"+item+"</option>"
					})
					$("#dong").html(options);
				}
			})
		})
		
		$("#dong").change(function(){
			$("[name='parkAddr']").val($("#dong").val())
			$("#ri").html="";
			var dong=$("#dong").val();
			var options="<option value=''></option>";
			$.ajax({
				url:"${pageContext.request.contextPath}/user/selectRi",
				data:"dong="+dong,
				dataType:"json",
				success: function(result){
					if(result){
						$.each(result, function(index, item){
							options+="<option value='"+item+"'>"+item+"</option>"
						})
					}
					$("#ri").html(options);
				}
			})
		})
		$("#ri").change(function(){
			$("[name='parkAddr']").val($("#ri").val())
		})
		///////////////////////시도구군동리 셀렉트바 끝/////////////////////
		$("#keyWord").change(function(){
			//alert("키워드 변경")
			$("#dataType").attr("name",$("#keyWord").val())
			if($("#dataType").attr("name")=='price'){
				$("#dataType").attr("type","number")
			}else{
				$("#dataType").attr("type","text")
			}
			//alert($("#dataType").attr("name"))
		})
		$("#search").click(function(){
			parkChange()
		})
		function parkChange(){
			
			var queryString = $("form[name=searchData]").serialize();
 			$.ajax({
				url:"${pageContext.request.contextPath}/user/renewParkList",
				data:queryString,
				dataType: "json",
				success : function(result){
					pager();
 					$.each(result, function(index, item){
						  var parkList = new Array();
						  parkList[0] = item.parkName;
						  parkList[1] = item.parkAddr;
						  parkList[2] = item.parkRegi.regiStart;
						  parkList[3] = item.parkRegi.regiEnd;
						  parkList[4] = item.price;
						  parkList[5] = item.carTypeLists;
						  parkList[6] = item.parkImg.imgPath
						  parkList[7] = item.parkNo
						  markerRenew(parkList)
					})
					removeMarker()
				},
				error:function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
				
			}) 
		}
		function markerRenew(parkList){
			console.log('마커리뉴호출')
			console.log(parkList)
			// 주소로 좌표를 검색합니다
	 		geocoder.addressSearch(parkList[1], function(result, status) {
			    // 정상적으로 검색이 완료됐으면 
			     if (status === daum.maps.services.Status.OK) {
			        var coords = new daum.maps.LatLng(result[0].y, result[0].x);
			        // 결과값으로 받은 위치를 마커로 표시합니다
			        var marker = new daum.maps.Marker({
			            map: map,
			            position: coords,
			            clickable: true          
			        });
			        markers.push(marker);
			     // 마커를 클릭했을 때 마커 위에 표시할 인포윈도우를 생성합니다
			     	var iwdata=""
			     		iwdata+="<div style='width: 600px;height: 230px; display: inline-block; border: 1px solid black;background: white; float: right;'>"
			     		iwdata+="<div style='width:40%; height: 100%; display: inline-block;'>"
			     		iwdata+="<img src='${pageContext.request.contextPath}/resources/images/park/"+parkList[6]+"' width='100%' height='100%'>"
			     		iwdata+="</div>"
			     		iwdata+="<div style='width:60%; height: 100%; display: inline-block; float: right'>"
			     		iwdata+="<ul class='list-group list-group-flush border'>"
			     		iwdata+="<a href='${pageContext.request.contextPath}/user/userReserveForm?parkNo="+parkList[7]+"'>"+"<li class='bg-primary text-white font-weight-bold text-center' style='font-size: 20px; padding: 7px'>"+parkList[0]+"</li></a>"
			     		iwdata+="<li class='list-group-item'>"+parkList[1]+"</li>"
			     		iwdata+="<li class='list-group-item'>"
			     		iwdata+=parkList[2]+"<br />~ "+parkList[3]
			     		iwdata+="</li>"
			     		iwdata+="<li class='list-group-item' > 가격: "+parkList[4]+" / "
			     		$.each(parkList[5],function(index,item){
			     			iwdata+= item.carType
			     			iwdata+= item.maxCar
						})
			     		iwdata+="</li></ul>"
			     		iwdata+="</div>"
			     		iwdata+="</div>"
			        var iwContent = iwdata
			        	, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
			            iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다
			        // 인포윈도우를 생성합니다
			        var infowindow = new daum.maps.InfoWindow({
			            content : iwContent,
			            removable : iwRemoveable
			        });
			        infowindows.push(infowindow);
			        // 마커에 클릭이벤트를 등록합니다
			        daum.maps.event.addListener(marker, 'click', function() {
			              // 마커 위에 인포윈도우를 표시합니다
			            exitInfoWindow();
			            infowindow.open(map, marker);
			        });      
			    } 
			});
		}
		function removeMarker() {
			 for ( var i = 0; i < markers.length; i++ ) {
			 markers[i].setMap(null);
			 infowindows[i].close();
			 } 
			 markers = [];
			 infowindows = [];
		}
		function exitInfoWindow(){
			for ( var i = 0; i < markers.length; i++ ) {
				infowindows[i].close();
			}
		}
		function pager(page){
			if(page==null){page=0}
			var queryString = $("form[name=searchData]").serialize();
 			$.ajax({
				url:"${pageContext.request.contextPath}/user/renewParkPager?cPage="+page,
				data:queryString,
				dataType: "json",
				success : function(result){
					$('#dataDiv').empty();
 					$.each(result[0], function(index, item){
 						  var parkList = new Array();
						  parkList[0] = item.parkName;
						  parkList[1] = item.parkAddr;
						  parkList[2] = item.parkRegi.regiStart;
						  parkList[3] = item.parkRegi.regiEnd;
						  parkList[4] = item.price;
						  parkList[5] = item.carTypeLists;
						  parkList[6] = item.parkImg.imgPath
						  parkList[7] = item.parkNo
					      var listData=" "
					      listData+="<div class='row' style='background-color:white; padding:2px; border:1px solid gray; height:200px'>"
					      listData+="<div class='col-sm-3'>"
					      listData+="<img class='img-responsive' src='${pageContext.request.contextPath}/resources/images/park/"+parkList[6]+"' style='width: 100%;height: 100%;'>"
					      listData+="</div>"
					      listData+="<div class='col-sm-8'>"
					      listData+="<h4><strong><a href='${pageContext.request.contextPath}/user/userReserveForm?parkNo="+parkList[7]+"'>"+parkList[0]+"</a></strong></h4>"
					      listData+="<h4><small>"+parkList[1]+"</small></h4>"
					      listData+="<h4><small>"+parkList[2]+" ~ "+parkList[3]+"</small>"
					      listData+="&nbsp;&nbsp;&nbsp;&nbsp;<small style='color: green; font: bold;'>시간당 "+parkList[4]+"원</small></h4><h4><small>"
					      $.each(parkList[5],function(index,item){
						    	 listData+= item.carType
						    	 listData+= item.maxCar
							  })
					      
					      listData+="</small></h4>"
					      listData+="</div><div class='col-sm-1'></div></div>"
					      $('#dataDiv').append(listData);
					})
					$('#pageBar').html(result[1]);
 					$(".page-link").on('click',$('document'),function(){
 						pager($(this).attr('id'))
 					})
				},
				error:function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
				
			})
		}
		parkChange();
	})
</script>
	<div class="container rounded" style="background-color: rgba( 0, 0, 0, .7 ); border: 1px solid gray">
		<form name="searchData">
			<br />
			<div class="row">
				<div class="col-sm-2">
					<select id="sido" class="custom-select">
						<option value="" disabled selected hidden>시/도</option>
						<c:forEach items="${sidoList}" var="sido">
							<option value="${sido}">${sido}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-sm-2">
					<select id="gugun" class="custom-select" >
						<option value="" disabled selected hidden>구/군</option>
					</select>
				</div>
				<div class="col-sm-2">
					<select id="dong" class="custom-select">
						<option value="" disabled selected hidden>동</option>
					</select>
				</div>
				<div class="col-sm-2">
					<select id="ri" class="custom-select">
						<option value="" disabled selected hidden>리</option>
					</select>
				</div>
				<div class="col-sm-2">
					<input type="text" name="reserveDate" readonly="readonly" class="form-control" placeholder="예약시간"/>
				</div>
			
				<div class="col-sm-2">
					<select name="carType" class="custom-select">
						<option value="" disabled selected hidden>차종</option>
						<option value="대형">대형</option>
						<option value="중형">중형</option>
						<option value="소형">소형</option>
					</select>
				</div>
				<div class="col-sm-12">
					<div id="map" style="width:100%;height: 400px; margin-top: 25px"></div>
				</div>
				<div class="col-sm-6">
					<input type="hidden" name="parkAddr"/>
					<input type="reset" class="btn btn-primary"value="초기화" />
				</div>
				<div class="col-sm-2">
					<select id="keyWord" class="custom-select" style="float: right">
						<option value="destination">목적지</option>
						<option value="parkName">주차장이름</option>
						<option value="price">가격(이하)</option>
						<option value="parkContent">내용</option>
					</select>
				</div>
				<div class="col-sm-4">
					<input type="text" name="" id="dataType" class="form-control" style="width:75%; display: inline-block;"/>
					<button type="button" class="btn btn-primary" id="search" style="float: right;width:25%;">검색</button>
				</div>
			</div>
			</form>
			<br />
			
			<div id="dataDiv">
			
			
			</div>
			<div id="pageBar">
			
			</div>
		</div>