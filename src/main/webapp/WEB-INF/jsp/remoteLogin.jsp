<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="css/remotelogin.css" />
<script type="text/javascript" src="script/lib/jquery-3-3-1.js"></script>

<title>登陆远程计算机</title>

<script >
	$(function(){
		$("#js-btn-login").click(function(){
			$.ajax({
				type:"POST",
				datatype:"text",
				url:"shellLogin",
				data:$("#loginForm").serializeArray(),
				contextType:"application/x-www-form-urlencoded",
				success:function(data){
					var result = eval("("+data+")");
					if(result.status == 200){
					
						alert("登陆成功！");
						
						if(parent && parent.refreshHosts){
							parent.refreshHosts();
						}
					}else{
						alert(result.msg);
					}
				}
			});
		});
		$("#js-btn-cancel").click(function(){
			if(parent && parent.refreshHosts){
				parent.refreshHosts();
			}
		});
	});

</script>

</head>
<body>
 <!-- -------登陆信息------ -->
    <div class="container">
	<section id="content">
		<form action="shellLogin" method="post" id="loginForm" >
			<h1>主机信息</h1>
			<div>
				<input type="text" placeholder="ip" required="" name="ip" id="ip" value="172.30.7.143" />
			</div>
			<div>
				<input type="text" placeholder="port" required="" name="port" id="port" value="22"/>
			</div>
			<div>
				<input type="text" placeholder="charset" required="" name="charset" id="charset" value="utf-8"/>
			</div>
			<div>
				<input type="text" placeholder="username" required="" name="username" id="username" value="root" />
			</div>
			<div>
				<input type="password" placeholder="password" required="" name="password" id="password" value="abc123!"/>
			</div>
			 <div class="">
				<span class="help-block u-errormessage" id="js-server-helpinfo">&nbsp;</span>			</div> 
			<div>
				<!-- <input type="submit" value="Log in" /> -->
				<input type="button" value="登录" class="btn btn-primary" id="js-btn-login"/> 
				<input type="button" value="取消" class="btn btn-primary" id="js-btn-cancel"/>
				<!-- <a href="#">Register</a> -->
			</div>
		</form><!-- form -->
		 <div class="button">
			<span class="help-block u-errormessage" id="js-server-helpinfo">&nbsp;</span>
		</div> 
	</section><!-- content -->
</div>
 
</html>