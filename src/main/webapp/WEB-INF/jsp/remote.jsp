<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
    <title>webssh 服务器监控管理工具</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="css/interactive.css" rel="stylesheet">
    <link rel="stylesheet" href="css/all.css" >
    
    <script type="text/javascript" src="script/lib/jquery-3-3-1.js"></script>
    <script type="text/javascript" src="script/lib/jquery.base64.js"></script>
        <script type="text/javascript" src="script/remote.js"></script>
    
	<script>
	    document.getElementsByTagName('body').height=window.innerHeight;
	</script>
	
	<script type="text/javascript">
		
		$(function(){
			refreshHosts();
			$("#cmdArea").bind("blur",function(){
				$("#cmdArea").unbind("keydown");
			});
			$("#cmdArea").bind("focus",function(){
				$("#cmdArea").bind("keydown",keyDownHandler);
			});
		});
	
	</script>
	<style type="text/css">
	#loginArea {
		position: absolute;
		z-index: 999;
		width: 430px;
		height: 550px;
		top: 20%;
		left: 40%;
		border: 2px;
		border-style: none;
		display:none;
	}
	</style>
</head>

<body class="box">
    <div class="leftbar">
        <ul>
            <li><i class="fas fa-user"></i></li>
            <li><i class="fas fa-users"></i></li>
            <li><i class="fas fa-smile"></i></li>
            <li><i class="fas fa-envelope"></i></li>
            <li><i class="fas fa-bell"></i></li>
            <li><i class="fas fa-calendar-alt"></i></li>
            <li><i class="fas fa-power-off"></i></li>
        </ul>
    </div>
    <div class="container">
        <div class="chatbox">
            <div class="chatleft">
                <div class="top">
                    <i class="fas fa-bars" style="font-size: 1.4em"></i>
                    <input type="text" placeholder="search" style="width: 140px; height: 36px; margin-left: 25px;">
                    <button class="searchbtn" onclick="sshlogin()"><i class="fas fa-search"></i></button>
                </div>
                <div class="center">
                    <ul id="hosts" name="hosts">
                    </ul>
                </div>
            </div>
            <div class="chatright">
                <div class="top">
                    <img style="border-radius: 20px; vertical-align: middle;width:55px;height:40px" src="images/server.png">
                    <span style="margin-left: 20px;" id="currentHost"></span>
                    <i class="fas fa-ellipsis-v" style="font-size: 1.4em; position: absolute; right: 20px; color: gray;"></i>
                </div>
                <div class="center">
                    <ul id="logArea" name="logArea">
                    </ul>
                </div>
                <div class="footer">
                    <textarea id="cmdArea" name="cmdArea" maxlength="800" rows="10" cols="40" style="width: 100%; resize: none; border: none; " placeholder="输入命令。。。"></textarea>
                    <button class="sendbtn" onclick="sendCmd()">发送</button>
                </div>
            </div>
        </div>
    </div>
   <div id="loginArea" ></div>
</body>
</html>
