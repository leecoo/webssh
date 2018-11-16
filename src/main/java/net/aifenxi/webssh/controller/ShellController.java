package net.aifenxi.webssh.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.JSchException;

import net.aifenxi.webssh.tools.ShellExecutorHelper;
import net.aifenxi.webssh.tools.ShellUtil;


@Controller
public class ShellController {
	@Value("${sellLog.filePath}")
	private String filePath;
	
	private String resultPattern = "\\[[0-9]*;*[0-9]*m";

	ShellUtil su = ShellUtil.getInstance();
	public static final String keySeperator = "###";
	
	
	/**
	 * 跳转到登陆页面，无处理
	 * @return
	 */
	@RequestMapping("/shellLoginPage")
	public String shellLoginPage() {
		
		return "remoteLogin";
	}
	
	/**
	 * SSH远程机器连接
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param charset
	 * @return
	 */
	@RequestMapping("/shellLogin")
	@ResponseBody
	public String shellLogin(@RequestParam(value="ip") String ip,@RequestParam("port") String port,@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("charset") String charset) {
		
		ShellExecutorHelper shellSession = su.getExecutor(genShellKeyString(this.getSession().getId(),ip));
		if(null == shellSession) {
			shellSession = su.createExecutor(ip, port, username, password,this.getSession().getId(),charset);
		}

		return "{'status':200,'msg':'login success'}";
	}
	
	/**
	 * 执行命令
	 * @param cmd
	 * @param ip
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@RequestMapping("/shellExcute")
	@ResponseBody
	public String shellExcute(@RequestParam(value="cmd")String cmd ,@RequestParam(value="ip") String ip) throws JSchException, IOException, InterruptedException {
		ShellExecutorHelper shellSession = su.getExecutor(genShellKeyString(this.getSession().getId(),ip));
		String file = filePath+this.genShellKeyString(this.getSession().getId(), ip);
		shellSession.executeCMD(cmd,file);
		
		return "{'status':200,'msg':'cmd send success!'}";
		
	}
	
	/**
	 * 获取命令执行后的结果，对于屏幕中的颜色进行过滤
	 * @param ip
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@RequestMapping("/getExecuteResult")
	@ResponseBody
	public String getExecuteResult(@RequestParam(value="ip") String ip) throws JSchException, IOException, InterruptedException {
		ShellExecutorHelper shellSession = su.getExecutor(genShellKeyString(this.getSession().getId(),ip));
		if(null == shellSession) {
			return "{'status':200,'msg':'getExecuteResult!','resultLog':'','resultLength':0}";
		}
		String file = filePath+this.genShellKeyString(this.getSession().getId(), ip);
		String resultMsg = shellSession.displayResult(file).replaceAll(resultPattern,"");
		System.out.println("resultMsg" +resultMsg);
		
		
		if(!"".equals(resultMsg)) {
			resultMsg = Base64Utils.encodeToString(resultMsg.replaceAll("\\n", " <br/>").getBytes("utf-8"));
		}
		System.out.println("resultMsg" +resultMsg);
		return "{'status':200,'msg':'getExecuteResult!','resultLog':'"+resultMsg+"','resultLength':"+resultMsg.length()+"}";
		
	}
	
	/**
	 * 获取当前用户所有登陆过的服务器信息
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@RequestMapping("/getLoginedHost")
	@ResponseBody
	public  String getLoginedHost() throws JSchException, IOException, InterruptedException {
		
		return "{'status':200,'msg':'getExecuteResult!',data:"+JSON.toJSONString(su.getHosts(this.getSession().getId()))+"}";
		
	}
	
	@RequestMapping("/hostLogout")
	@ResponseBody
	public  String hostLogout(@RequestParam(value="ip") String ip) throws JSchException, IOException, InterruptedException {
		ShellExecutorHelper shellSession = su.getExecutor(genShellKeyString(this.getSession().getId(),ip));
		shellSession.close();
		su.removeExecutor(genShellKeyString(this.getSession().getId(),ip));
		System.out.println("hostLogout .... ");
		return "{'status':200,'msg':'hostLogout!'}";
		
	}

	
	/**
	 * 以下是工具类
	 * @return
	 */
	private HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		return request;
	}
	
	private Object getRequestParameter(String keyStr) {
		return this.getRequest().getAttribute(keyStr);
	}
	
	private HttpSession getSession() {
		return this.getRequest().getSession();
	}
	
	public static String genShellKeyString(String sessionid ,String hostip) {
		return sessionid+keySeperator+hostip;
	}
	
}
