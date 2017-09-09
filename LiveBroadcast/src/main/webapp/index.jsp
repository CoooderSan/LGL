<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html><head>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.css">
<script type="text/javascript" src="js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" >
	var ajaxC ;
	
	$(function(){
		$.ajax({
			url:"liveInit/initC",
			success:function(date){
				var date = eval(date);
				for(var i = 0;i<date.length;i++){
					$("#imgList").append("<div style='width:25%;float:left'><img style='margin-left:5%;width:95%' src='"+date[i].img+"' class='img-thumbnail'/>"
					+"<div style='margin:2% 2% 2% 2%'><span class='label label-warning' style='margin-left:5%'>"+date[i].title+"</span><br/>"
					+"<button style='margin-left:80%' type='button' class='btn btn-primary btn-xs'>"
					+"<span class='glyphicon glyphicon-user'></span>"+(date[i].viewers)/10000+"万"
					+"</button></div>"
					+"</div>")
				}
			}
		})
	})
</script>


<body>
<div class="container">
	<ul class="nav nav-tabs" style="margin-bottom: 1%">
		<li class="active"><a href="#">LOL</a></li>
		<li><a href="#">Dota</a></li>
		<li><a href="#">守望先锋</a></li>
	</ul>
	<div id ="imgList"></div>
</div>
</body>

</html>
