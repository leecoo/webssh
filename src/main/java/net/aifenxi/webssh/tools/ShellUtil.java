package net.aifenxi.webssh.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.JSchException;

import net.aifenxi.webssh.controller.ShellController;


public class ShellUtil {

	private ShellUtil() {
		
	}
	
	private static ShellUtil su = new ShellUtil();
	
	private static Map<String,ShellExecutorHelper> shellSessions = new HashMap<String,ShellExecutorHelper>();

	public static ShellUtil getInstance() {
		return su;
	}
	/**
	 * 获取服务器连接的Session
	 * @param key
	 * @return
	 */
	public ShellExecutorHelper getExecutor(String key) {
		ShellExecutorHelper s = shellSessions.get(key);
		return s;
	}
	
	/**
	 * 创建服务器连接Session
	 * @param ip
	 * @param portstr
	 * @param name
	 * @param password
	 * @param sessionid
	 * @param charset
	 * @return
	 */
	public ShellExecutorHelper createExecutor(String ip,String portstr,String name,String password,String sessionid,String charset) {
		int port = 22;
		if(portstr != null && !"".equals(portstr))
			port = Integer.parseInt(portstr);
		ShellExecutorHelper seh = null;
		try {
			seh = new ShellExecutorHelper(ip, port, name, password,charset);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		if ( seh != null )
		{
			this.shellSessions.put(ShellController.genShellKeyString(sessionid, ip), seh);
		}
		return seh;
		
	}
	
	public void removeExecutor(String key) {
		this.shellSessions.remove(key);
	}
	
	/**
	 * 获取存储的已经登陆的服务器信息
	 * @param sessionid
	 * @return
	 */
	public List<String> getHosts(String sessionid) {
		Iterator<String> iter = shellSessions.keySet().iterator();
		List<String> result = new ArrayList<String> ();
		while(iter.hasNext()) {
			String key = iter.next();
			if(key.startsWith(sessionid)) {
				result.add(key.split(ShellController.keySeperator)[1]);
			}
		}
		return result;
	}
	
	
}
