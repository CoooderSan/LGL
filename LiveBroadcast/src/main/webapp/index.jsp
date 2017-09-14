<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html lang="zh"><head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="msapplication-tap-highlight" content="no">
<!-- Favicons-->
<link rel="apple-touch-icon-precomposed" href="http://materializecss.com/images/favicon/apple-touch-icon-152x152.png">
<meta name="msapplication-TileColor" content="#FFFFFF">
<meta name="msapplication-TileImage" content="images/favicon/mstile-144x144.png">
<link rel="icon" href="http://materializecss.com/images/favicon/favicon-32x32.png" sizes="32x32">
<!--  Android 5 Chrome Color-->
<meta name="theme-color" content="#EE6E73">
<!-- CSS-->
<link href="css/prism.css" rel="stylesheet">
<link href="css/materialize.min.css" rel="stylesheet" type="text/css">
<link href="css/ghpages-materialize.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="css/css.css" rel="stylesheet" type="text/css">
<link href="css/icon.css" rel="stylesheet">
<script>
    window.liveSettings = {
        api_key: "a0b49b34b93844c38eaee15690d86413",
        picker: "bottom-right",
        detectlang: true,
        dynamic: true,
        autocollect: true
    };
</script>
    <script src="js/live.js"></script><style type="text/css">.txlive {display: none;}
</style>
<script type="text/javascript" src="js/jquery-2.0.3.min.js"></script>
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
			for(var j=0;j<dateList.length;j++){
				if("" != dateList[j] && null != dateList[j] && undefined != dateList[j]){
					allLiveList = allLiveList.concat(dateList[j]);
					date = dateList[j];
				}	
			}
			pageInfo = pageInfo<0?1:pageInfo;
			pageInit = pageCount*(pageInfo-1);5
			pageLength = allLiveList.length>pageCount*pageInfo?pageCount*pageInfo:allLiveList.length;
			var htmlMess ="";
			for(var i = pageInit;i<pageLength;i++){
				if(i%3 == 0){
					htmlMess +="<div class='row'>";
				}
				htmlMess +="<div class='col s12 m4'>"
		            +"<div class='card  hoverable'>"
		                +"<div class='card-image'>"
		                    +"<img src='"+allLiveList[i].img+"' class='responsive-img'>"
		                    +"<a onclick=toLive('"+allLiveList[i].roomUrl+"') class='btn-floating btn-large halfway-fab waves-effect waves-light red' href='#'><i class='material-icons'>games</i></a>"
		                +"</div>"
		                +"<div class='card-content'>"
		                    +"<div class='card-title activator grey-text text-darken-4'><h5 class='truncate' style='font-weight:bold' >"+allLiveList[i].title+"</h5></div>"
		                	+"<div>"
		                		+"<span><i class='material-icons' style='vertical-align:middle;'>perm_identity</i>&nbsp;"+allLiveList[i].broadcaster+"</span>"
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
			if(type == 0){
				var pageNum = Math.ceil(allLiveList.length/pageCount);
				$("#ulPage").empty();
				$("#ulPage").append("<li class='waves-effect' onclick=LiveTypeInfo('"+liveType+"',1,1)><a href='#'><i class='material-icons'>chevron_left</i></a></li>");
				for(var i=0;i<pageNum;i++){
					$("#ulPage").append("<li class='waves-effect' onclick=LiveTypeInfo('"+liveType+"',"+(i+1)+",1)><a href='#'>"+(i+1)+"</a></li>");
				}
				$("#ulPage").append("<li class='waves-effect' onclick=LiveTypeInfo('"+liveType+"',"+pageNum+",1)><a href='#'><i class='material-icons'>chevron_right</i></a></li>");
				
			}
		}else{
			$("#imgList").empty();
			$("#ulPage").empty();
		}
		
	}
</script>


<body>
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
                <input id="search"><i class="material-icons">search</i>
                <div class="search-results"></div>
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


</main>
<footer>
        <ul id = "ulPage" class="pagination" style="text-align: center"></ul>


</footer>
<!--  Scripts-->
<script src="js/jquery-3.js"></script>
<script>if (!window.jQuery) { document.write('<script src="js/jquery-3.2.1.min.js"><\/script>'); }
</script>
<script src="js/jquery.js"></script>
<script src="js/prism.js"></script>
<script src="js/lunr.js"></script>
<script src="js/search.js"></script>
<script src="js/materialize.js"></script>
<script src="js/init.js"></script>
</body>

</html>
