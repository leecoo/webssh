package net.aifenxi.webssh.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ShellExecutorHelper {
	
	private final static Log logger =LogFactory.getLog(SSHHelper.class);

    private String  charset = Charset.defaultCharset().toString();
    private Session session = null;
    private ChannelShell channel = null;
    private boolean isBlocked = false;
    /**
     * 调用回显时从已经现实的位置向下读取，这个是 游标
     */
    private int cursor = 0;
    
    

    public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public ShellExecutorHelper(String host, Integer port, String user, String password) throws JSchException {
        connect(host, port, user, password);
    } 
	
	public ShellExecutorHelper(String host, Integer port, String user, String password ,String encode) throws JSchException {
		this.charset = encode;
        connect(host, port, user, password);
    } 
	
    /**
     * 连接sftp服务器
     * @param host 远程主机ip地址
     * @param port sftp连接端口，null 时为默认端口
     * @param user 用户名
     * @param password 密码
     * @return
     * @throws JSchException 
     */
    private Session connect(String host, Integer port, String user, String password) throws JSchException{
        try {
            JSch jsch = new JSch();
            if(port != null){
                session = jsch.getSession(user, host, port.intValue());
            }else{
                session = jsch.getSession(user, host);
            }
            session.setPassword(password);
            //设置第一次登陆的时候提示，可选值:(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //30秒连接超时
            session.connect(5000);



        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println("SFTPUitl 获取连接发生错误");
            throw e;
        }
        return session;
    }
    
    /**
     * 执行命令，并将执行结果保存到指定文件中
     * @param cmd
     * @param outputFile
     * @throws JSchException
     * @throws IOException
     * @throws InterruptedException
     */
    public void executeCMD(String cmd ,String outputFile) throws JSchException, IOException, InterruptedException {
    	if(null == channel) {
    		channel = (ChannelShell) session.openChannel("shell");
    		channel.connect();
    	}
    	
        OutputStream outputStream = channel.getOutputStream();
        String command = cmd +" \n\r";
        this.saveResult(outputFile);
        outputStream.write(command.getBytes());
        outputStream.flush();
    }
    
    /**
     * 保存结果到指定文件中
     * @param outputFile
     * @throws IOException
     */
    private void saveResult(String outputFile) throws IOException {
    	if(isBlocked) {
    		return;
    	}
    	isBlocked = true;
    	
    	ShellLogThread slt = new ShellLogThread(this.channel,outputFile,charset);
    	slt.start();

    }
    
    /**
     * 回显执行结果，从指定文件中读取
     * @param file
     * @return
     */
    public String displayResult(String file) {
    	if(!(new File(file)).exists()) {
    		return "";
    	}
    	StringBuffer buffer = new StringBuffer();
    	int i=0;
    	int currentCursor = 0;
    	String msg = null;
    	FileReader reader = null;
    	BufferedReader re =null;
    	try {
    		
			reader = new FileReader(file);
			re = new BufferedReader(reader);
			while((msg = re.readLine())!=null && i<100) {
				if(currentCursor <= this.getCursor()) {
					currentCursor ++;
					continue;
				}
				buffer.append(msg).append(" \n ");
				i++;
			}
			re.close();
			reader.close();
			this.setCursor(this.getCursor()+i);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return buffer.toString();
    }
    
    /**
     * 用完记得关闭，否则连接一直存在，程序不会退出
     */
    public void close(){
        if(session.isConnected())
        session.disconnect();
     }
    
    /**
     * 回收时强制销毁
     * @param args
     */
    
    @Override
    protected void finalize() throws Throwable {
    	
    	if( null != channel && channel.isConnected()) {
    		channel.disconnect();
    		channel = null;
    	}
    	
    	if(null != session && session.isConnected()) {
    		session.disconnect();
    		session = null; 
    	}
    	
    	super.finalize();
    	
    }

}
