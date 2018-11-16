/**
 * 
 */

function sshlogin(){
			$("#loginArea").css("display","block");
			$("#loginArea").html('<iframe src="shellLoginPage"  width=100%  height=100%   frameborder="1"  marginheight="0"       marginwidth="0" style="border:0px none transparent"></iframe>'); 
		}
		
		function refreshHosts(){
			$("#loginArea").html('');
			$("#loginArea").css("display","none");
			
			$.ajax({
				type:"GET",
				datatype:"text",
				url:"getLoginedHost",
				contextType:"application/x-www-form-urlencoded",
				success:function(data){
					var result = eval("("+data+")");
					if(result.status == 200){
						var innerStr = "";
						for(var i=0; i<result.data.length; i++){
							innerStr += createHostsInfo(result.data[i]);
						}
						$("#hosts").html(innerStr);
						innerStr = null;
					}else{
						alert(result.msg);
					}
				}
			});
		}
		
		
		function createHostsInfo(host){
			var html = '<li ondblclick="selectHost(this)" id="'+host+'" > <img style="border-radius: 20px; vertical-align: middle;width:55px;height:40px;" src="images/server.png"> <span style="margin-left: 10px;">'+host+'</span> <img onclick="closeHost(this)" style="border-radius: 20px; vertical-align: middle;width:60px;height:60px; margin-top:-10px;float:right;" src="images/closeicon.png"> </li>';
			return html;
		}
		
		var currentSelectedHost ;
		function selectHost($this){
			if(currentSelectedHost){
				if(existTimer()){
					clearQueryTimer();
				}
				$(currentSelectedHost).css("background-color","white");
			}
			currentSelectedHost = $this;
			$($this).css("background-color","lightblue");
			$("#currentHost").html($($this).attr("id"));
			
			/**
			**清楚log日志区域
			*/
			clearLogArea();
			
			//setQueryTimer();
		}
		function keyDownHandler(event){
			if(event.keyCode == 13){
				sendCmd();
			}
		}
		
		function sendCmd(){
			if(! currentSelectedHost ){
				alert("请选择主机");
				return;
			}
			ip = $(currentSelectedHost).attr("id");
			cmd = $("#cmdArea").val();
			dataObj = {};
			dataObj.ip=ip;
			dataObj.cmd=cmd;
			$.ajax({
				type:"POST",
				datatype:"text",
				url:"shellExcute",
				data:dataObj,
				contextType:"application/x-www-form-urlencoded",
				success:function(data){
					var result = eval("("+data+")");
					$("#cmdArea").val("");
				}
			});
			/**
			*获取执行结果
			**/
			 if(! existTimer()){
				setQueryTimer();
			}  
			
		}
		
		function clearLogArea(){
			$("#logArea").html("");
		}
		
		function queryExceuteResult(){
			var dataObj = {};
			dataObj.ip=$(currentSelectedHost).attr("id");
			$.ajax({
				type:"POST",
				datatype:"text",
				url:"getExecuteResult",
				data:dataObj,
				contextType:"application/x-www-form-urlencoded",
				success:function(data){
					var result = eval("("+data+")");
					if(result.status == 200){
						if(result.resultLength > 0){
							$("#logArea").html($("#logArea").html()+" \n "+$.base64.decode(result.resultLog,'utf-8'));	
						//	$("#logArea").html($("#logArea").html()+" \n "+result.resultLog);	

						}
					}else{
						alert(result.msg)
					}
				}
			}); 
		}
		
		var globalInterval = {};
		function setQueryTimer(){
			var intervalId = setInterval("queryExceuteResult()",1000);
			globalInterval.id = intervalId;
			globalInterval.ip = currentSelectedHost;
		}
		
		function clearQueryTimer(){
			clearInterval(globalInterval.id);
			globalInterval.id = null;
			globalInterval.ip = null;
		}
		
		function existTimer(){
			if(globalInterval.ip == currentSelectedHost){
				return true;
			}
		}
		
		function closeHost($this){
			var resu = confirm("关闭主机","sdfsdf");
			if(resu == true){
				var dataObj = {};
				dataObj.ip=$($this).parent().attr("id");
				$.ajax({
					type:"POST",
					datatype:"text",
					url:"hostLogout",
					data:dataObj,
					contextType:"application/x-www-form-urlencoded",
					success:function(data){
						var result = eval("("+data+")");
						if(result.status == 200){
							refreshHosts();
						}else{
							alert(result.msg)
						}
					}
				}); 
			}
		}
		