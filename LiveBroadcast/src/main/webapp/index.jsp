<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html><head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=0">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/container.css">
<script type="text/javascript" src="js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" >
	var returnLiveInfo ;
	var pageCount = 12;
	$(function(){
		$.ajax({
			url:"liveInit/initC",
			success:function(date){	
				var date = eval(date);
				returnLiveInfo = date;
				LiveTypeInfo("LOL",1,0);
			}
		})
	})
	function toLive(roomUrl){
		window.open(roomUrl);
	}
	function LiveTypeInfo(liveType,pageInfo,type){
		var dateList;
		var date;
		switch (liveType) {
			case "LOL":
			$("#li1").attr("class","active");
			$("#li2").attr("class","");
			$("#li3").attr("class","");
			dateList = returnLiveInfo.LOLInfo;	
				break;
			case "Dota":
			dateList = returnLiveInfo.DOTAInfo;	
			$("#li1").attr("class","");
			$("#li2").attr("class","active");
			$("#li3").attr("class","");	
				break;
			case "hearthstone":
			dateList = returnLiveInfo.hearthstoneInfo;
			$("#li1").attr("class","");
			$("#li2").attr("class","");
			$("#li3").attr("class","active");		
				break;
		}
		dateList = eval(dateList);
		if("" != dateList && null != dateList && undefined != dateList){
			$("#imgList").empty();
			var allLiveList = new Array();
			for(var j=0;j<dateList.length;j++){
				if("" != dateList[j] && null != dateList[j] && undefined != dateList[j]){
					allLiveList = allLiveList.concat(dateList[j]);
					date = dateList[j];
				}	
			}
			pageInfo = pageInfo<0?1:pageInfo;
			pageInit = pageCount*(pageInfo-1);
			pageLength = allLiveList.length>pageCount*pageInfo?pageCount*pageInfo:allLiveList.length;
			for(var i = pageInit;i<pageLength;i++){
				$("#imgList").append("<div onclick=toLive('"+allLiveList[i].roomUrl+"') class='col-md-3 col-sm-6'><div class='item-panel panel b-a'><img style='margin-left:5%;width:95%' src='"+allLiveList[i].img+"' class='img-thumbnail'/>"
				+"<div ><span class='label label-warning' >"+allLiveList[i].title+"</span><br/>"
				+"<span class='glyphicon glyphicon-user' style='font-size: 2px'></span>"+allLiveList[i].broadcaster
				+"<button style='font-size: 2px' type='button' class='btn btn-primary btn-xs'>"
				+"<span class='glyphicon glyphicon-user'></span>"+(allLiveList[i].viewers)/10000+"万"
				+"</button></div>"
				+"</div></div>");
			}
			if(type == 0){
				var pageNum = Math.ceil(allLiveList.length/pageCount);
				$("#ulPage").empty();
				$("#ulPage").append("<li onclick=LiveTypeInfo('"+liveType+"',1,1)><a href='#'>&laquo;</a></li>");
				for(var i=0;i<pageNum;i++){
					$("#ulPage").append("<li onclick=LiveTypeInfo('"+liveType+"',"+(i+1)+",1)><a href='#'>"+(i+1)+"</a></li>");
				}
				$("#ulPage").append("<li onclick=LiveTypeInfo('"+liveType+"',"+pageNum+",1)><a href='#'>&raquo;</a></li>");
			}
		}
		
	}
</script>


<body>
<div class='container'>
	<ul class="nav nav-tabs" style="margin-bottom: 1%">
		<li id="li1" class="active" onclick="LiveTypeInfo('LOL',1,0)"><a href="#">LOL</a></li>
		<li id="li2" onclick="LiveTypeInfo('Dota',1,0)"><a href="#">Dota</a></li>
		<li id="li3" onclick="LiveTypeInfo('hearthstone',1,0)"><a href="#">炉石传说</a></li>
	</ul>
	<div style="margin-top: 10%" id ="imgList"></div>
</div>
<div style="text-align: center;">
	<ul class="pagination pagination-lg" id ="ulPage">
	</ul>
</div>
</body>

</html>
