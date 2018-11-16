package net.aifenxi.webssh.tools;

import com.jcraft.jsch.JSchException;

public class SSHTest {

	    public static void main(String[] args) {
	        testSSH();
	    }

	    public static void testSSH(){
	        try {
	            //使用目标服务器机上的用户名和密码登陆
	            SSHHelper helper = new SSHHelper("172.30.7.143", 22, "root", "abc123!");
	            String command = "more install.log ";
	            try {
	                SSHResInfo resInfo = helper.sendCmd(command);
//	                System.out.println(resInfo.toString());
	                helper.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        } catch (JSchException e) {
	            e.printStackTrace();
	        }
	    }
	
}
