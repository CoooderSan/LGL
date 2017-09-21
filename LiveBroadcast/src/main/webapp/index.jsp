<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html lang="zh"><head>
<title>LGL</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="msapplication-tap-highlight" content="no">
<!-- Favicons-->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="apple-touch-icon-precomposed" href="image/LGLTitleS.png">
<meta name="msapplication-TileColor" content="#FFFFFF">
<meta name="msapplication-TileImage" content="image/LGLTitleS.png">
<link rel="icon" href="image/LGLTitleS.png" sizes="32x32">
<!--  Android 5 Chrome Color-->
<meta name="theme-color" content="#EE6E73">
<!-- CSS-->
<link href="css/prism.css" rel="stylesheet">
<link href="css/materialize.min.css" rel="stylesheet" type="text/css">
<link href="css/ghpages-materialize.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="css/css.css" rel="stylesheet" type="text/css">
<link href="css/icon.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/normalize.css" />
<link rel="stylesheet" href="css/style.css">
<script>
    window.liveSettings = {
        api_key: "a0b49b34b93844c38eaee15690d86413",
        picker: "bottom-right",
        detectlang: true,
        dynamic: true,
        autocollect: true
    };
</script>
<script type="text/javascript" src="js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" >
	var returnLiveInfo ;
	var pageCount = 12;
	var roomsTypeNow = "";
	var pageNum = 0;
	$(function(){
		$.ajax({
			url:"liveInit/initC",
			success:function(date){
				var date = eval(date);
				returnLiveInfo = date;
				LiveTypeInfo("LOL",1,0);
			}
		})
		$("#searchI").click(function(){
			LiveTypeInfo(roomsTypeNow,1,0,$("#search").val());
			
		})
		$("body").keydown(function(e) {
			 var curKey = e.which;
             if(curKey == 13){
				LiveTypeInfo(roomsTypeNow,1,0,$("#search").val());
				return false;
			 }
        });
        $("#plus_ten").click(function(){
        	if($("#errorValue").html().substring(0,$("#errorValue").html().length-1) >= 100){
        		Materialize.toast('加载好了也没什么卵用，兄弟！', 4000);
        	}
        });
        $('body').on("click",'.heart',function()
        {
            if($(this).attr("rel") === 'like')
            {
                $(this).addClass("heartAnimation").attr("rel","unlike");

            }
            else
            {
                $(this).removeClass("heartAnimation").attr("rel","like");
                $(this).css("background-position","left");
            }
        });
	})
	$(document).click(function(e) { // 在页面任意位置点击而触发此事件
		var clickedNode = event.target;
		var parentNode = event.target.parentNode;
		var parentNodeParent = event.target.parentNode.parentNode;
		var NodeType = event.target.nodeName;
		var parentNodeType = event.target.parentNode.nodeName;
		var parentNodeParentType = event.target.parentNode.parentNode.nodeName;
 	 	if(parentNode.className == "waves-effect" || parentNodeParent.className == "waves-effect"){
 	 		$("#ulPage").children("li").attr("class","waves-effect");
 	 		if(parentNodeParent.className == "waves-effect"){
	 	 		if(parentNodeParent.id == "left"){
	 	 			$("#ulPage").children("li")[1].className = "active";
	 	 		}else if(parentNodeParent.id == "right"){
	 	 			$("#ulPage").children("li")[pageNum].className = "active";
	 	 		}
	 	 		parentNodeParent.className = "waves-effect";
	 	 	}else if(parentNode.className == "waves-effect"){
	 	 		if(parentNode.id == "left"){
	 	 			$("#ulPage").children("li")[1].className = "active";
	 	 		}else if(parentNode.id == "right"){
	 	 			$("#ulPage").children("li")[pageNum].className = "active";
	 	 		}else{
	 	 			parentNode.className = "active";
	 	 		}
 	 			
	 	 	}
 	 	}
	})
	
	function toLive(roomUrl){
		window.open(roomUrl);
	}
	function LiveTypeInfo(liveType,pageInfo,type,searchContent){
		$("#errorPage").attr("style","display:none");
		$("#errorValue").html("0%");
		$("#errorValue").parent().attr("style","width:0%");
		roomsTypeNow = liveType;
		var dateList;
		var date;
		$(window).scrollTop(0);
        $(window).scrollLeft(0);
		switch (liveType) {
			case "LOL":
			$("#li1").attr("class","bold active");
			$("#li2").attr("class","bold");
			$("#li3").attr("class","bold");
			$("#li4").attr("class","bold");
			$("#pageTitleName").html("英雄联盟");
			dateList = returnLiveInfo.LOLInfo;
				break;
			case "Dota":
			dateList = returnLiveInfo.DOTAInfo;
			$("#li1").attr("class","bold");
			$("#li2").attr("class","bold");
			$("#li3").attr("class","bold active");
			$("#li4").attr("class","bold");
			$("#pageTitleName").html("DOTA2");
				break;
			case "hearthstone":
			dateList = returnLiveInfo.hearthstoneInfo;
			$("#li1").attr("class","bold");
			$("#li2").attr("class","bold active");
			$("#li3").attr("class","bold");
			$("#li4").attr("class","bold");
			$("#pageTitleName").html("炉石传说");
				break;
			case "kingG":
			dateList = returnLiveInfo.kingGInfo;
			$("#li1").attr("class","bold");
			$("#li2").attr("class","bold");
			$("#li3").attr("class","bold");
			$("#li4").attr("class","bold active");
			$("#pageTitleName").html("王者荣耀");
				break;
		}
		if("" != dateList && null != dateList && undefined != dateList){
			dateList = eval(dateList);
			$("#imgList").empty();
			var allLiveList = new Array();
			var dateListIsNullCount = 0;
			for(var j=0;j<dateList.length;j++){
				if("" != dateList[j] && null != dateList[j] && undefined != dateList[j]){
					allLiveList = allLiveList.concat(dateList[j]);
					date = dateList[j];
				}else{
					dateListIsNullCount++;
				}
			}
			if(dateListIsNullCount == dateList.length){
				$("#ulPage").empty();
				$("#errorPage").attr("style","display:block");
				return;
			}
			if(searchContent != undefined && searchContent != ""){
				//根据房间名和主播搜索直播间
				var allLiveList = allLiveList.filter(function(a) {
				    return ((a.broadcaster.indexOf(searchContent) > -1)||(a.title.indexOf(searchContent) > -1))?true:false;
				});
				if(allLiveList == ""){
					$("#ulPage").empty();
					$("#errorPage").attr("style","display:block");
					return;
				}
			}else{
				$("#search").val("");
			}
			
			//按人气降序排序
			allLiveList.sort(function(a,b){
				return a.viewers-b.viewers
			}).reverse();
			
			pageInfo = pageInfo<0?1:pageInfo;
			pageInit = pageCount*(pageInfo-1);
			pageLength = allLiveList.length>pageCount*pageInfo?pageCount*pageInfo:allLiveList.length;
			var htmlMess ="";
			for(var i = pageInit;i<pageLength;i++){
				if(i%3 == 0){
					htmlMess +="<div class='row'>";
				}
				htmlMess +="<div class='col s12 m4'>"
		            +"<div class='card  hoverable'>"
		                +"<div class='card-image'>"
		                    +"<img id='img"+i+"' src='image/initImage.png' class='responsive-img'>"
		                    +"<a onclick=toLive('"+allLiveList[i].roomUrl+"') class='btn-floating btn-large halfway-fab waves-effect waves-light red' href='#'><i class='material-icons'>games</i></a>"
		                +"</div>"
		                +"<div class='card-content'>"
		                    +"<div class='card-title activator grey-text text-darken-4'><h5 class='truncate' style='font-weight:bold' >"+allLiveList[i].title+"</h5></div>"
		                	+"<div>"
		                		+"<a class='heart' rel='like' style='bottom: -4%;margin-left:-1%'></a>"
		                		+"<span style='margin-left:7%'><i class='material-icons' style='vertical-align:middle;'>perm_identity</i>&nbsp;"+allLiveList[i].broadcaster+"</span>"
		                		+"<span style='float: right;'><i class='material-icons' style='vertical-align: middle;'>visibility</i>&nbsp;"+(allLiveList[i].viewers/10000)+"万</span>"
		            		+"</div>"
		            	+"</div>"
		            +"</div>"
		        +"</div>"
		        if((i-2)%3 == 0){
		        	htmlMess +="</div>";
		        }
			}
			$("#imgList").append(htmlMess);
			setTimeout(function(){
				for(var i = pageInit;i<pageLength;i++){
					if(document.getElementById("img"+i).complete){
        				$("#img"+i).attr("src",allLiveList[i].img);
        			}
				}
        	},10)
			if(type == 0){
				pageNum = Math.ceil(allLiveList.length/pageCount);
				$("#ulPage").empty();
				$("#ulPage").append("<li id='left' class='waves-effect' onclick=LiveTypeInfo('"+liveType+"',1,1)><a href='#'><i class='material-icons'>chevron_left</i></a></li>");
				for(var i=0;i<pageNum;i++){
					if(i == 0){
						$("#ulPage").append("<li class='active' onclick=LiveTypeInfo('"+liveType+"',"+(i+1)+",1)><a href='#'>"+(i+1)+"</a></li>");
					}else{
						$("#ulPage").append("<li class='waves-effect' onclick=LiveTypeInfo('"+liveType+"',"+(i+1)+",1)><a href='#'>"+(i+1)+"</a></li>");
					}
				}
				$("#ulPage").append("<li id='right' class='waves-effect' onclick=LiveTypeInfo('"+liveType+"',"+pageNum+",1)><a href='#'><i class='material-icons'>chevron_right</i></a></li>");

			}
		}else{
			$("#imgList").empty();
			$("#ulPage").empty();
			$("#errorPage").attr("style","display:block");
		}
	}
</script>


<body id="bodyId" oncontextmenu="return false" ondragstart="return false" onselectstart="return false" onselect="document.selection.empty()" oncopy="document.selection.empty()" onbeforecopy="return false" onmouseup="document.selection.empty()">
<header>
    <!--title-->
    <nav class="top-nav">
        <div class="container">
            <div class="nav-wrapper">
                <a id="pageTitleName" class="page-title" style="font-weight:bold"></a>
            </div>
        </div>
    </nav>
    <!--左侧导航栏-->
    <div class="container"><a href="#" data-activates="nav-mobile" class="button-collapse top-nav waves-effect waves-light circle hide-on-large-only"><i class="material-icons">menu</i></a></div>
    <ul id="nav-mobile" class="side-nav fixed" style="transform: translateX(0%);">
        <li class="logo"><a id="logo-container"  class="brand-logo">
            <object id="front-page-logo" type="image/svg+xml" data="image/lgl.svg">Your browser does not support SVG</object></a></li>
        <li class="search">
            <div class="search-wrapper card">
                <input id="search"><i id="searchI" class="material-icons">search</i>
            </div>
        </li>
        <li id="li1" onclick="LiveTypeInfo('LOL',1,0)" class="bold"><a  class="waves-effect waves-teal" style="font-weight:bold">英雄联盟</a></li>
        <li id="li2" onclick="LiveTypeInfo('hearthstone',1,0)" class="bold"><a  class="waves-effect waves-teal" style="font-weight:bold">炉石传说</a></li>
        <li id="li3" onclick="LiveTypeInfo('Dota',1,0)" class="bold"><a class=" waves-effect waves-teal" style="font-weight:bold">DOTA2</a></li>
        <li id="li4" onclick="LiveTypeInfo('kingG',1,0)" class="bold"><a class=" waves-effect waves-teal " style="font-weight:bold">王者荣耀</a></li>
    </ul>
</header>
<main>
<div id="imgList" class="container">
    <!--房间列表-->
</div>
 <div id="errorPage" style="display: none;" >
    <div id="loadingContainer" >
        <div class="loadingbar" >
            <div class="marker_container">
                <div class="marker"></div>
                <div class="marker"></div>
                <div class="marker"></div>
                <div class="marker"></div>
            </div>
            <div class="filler_wrapper">
                <div class="filler">
                    <span id="errorValue" class="value">0%</span>
                </div>
            </div>
        </div>
    </div>
    <div style="position: absolute; left: 52%;">
        <a id="plus_ten" class="tooltipped btn-floating waves-effect waves-light red z-depth-5" style="width: 120px;height: 120px;padding: 30px;border-radius: 50%;top: -10%"; data-position="bottom"; data-delay="50"; data-tooltip="加载不出来，请点击加速~">
            <span style="display:block;;">
                <i class="large material-icons" style="vertical-align: middle;">fingerprint</i>
            </span>
        </a>
    </div>
</div>
</main>
<footer>
        <ul id = "ulPage" class="pagination" style="text-align: center"></ul>


</footer>
<!--  Scripts-->
<script src="js/jquery-3.js"></script>
<script>if (!window.jQuery) { document.write('<script src="js/jquery-3.2.1.min.js"><\/script>'); }
</script>
<!--错误页面js-->
<script type="text/javascript" src="js/blower-loading.js"></script>
<script src="js/jquery.js"></script>
<script src="js/prism.js"></script>
<script src="js/lunr.js"></script>
<script src="js/search.js"></script>
<script src="js/materialize.js"></script>
<script src="js/init.js"></script>
</body>

<!--错误页面方法-->
<script type="text/javascript">
    var blower = null;
    $(document).ready(function () {
        blower = new LoadingBlower("#loadingContainer");
        $("#plus_ten").on("click", function () {
            blower.addProgress(0.1);
        });
    });
</script>
</html>
