package net.aifenxi.webssh.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelShell;

public class ShellLogThread extends Thread {

	InputStream inputStream = null;
	ChannelShell channel = null;
	String file= null;
	String charset = null;
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public ShellLogThread(ChannelShell ch) {
		this.channel=ch;
	}
	public ShellLogThread(ChannelShell ch,String outpuFile,String encode) {
		this.channel=ch;
		this.file = outpuFile;
		this.charset = encode;
	}
	
	
	/**
	 * 执行命令主体，在执行中判断传入的流走向，是文件就存入文件，否则输出到控制台
	 */
	@Override
	public void run() {

    	BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(channel.getInputStream(),charset));
		
			if(null != file) {
				outputWrite(in);
    		}else {
    			outputPrint(in);
    		}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	super.run();
	}
	/**
	 * 输出到控制台
	 * @param in
	 * @throws IOException
	 */
	private void outputPrint(BufferedReader in) throws IOException {
		String msg = null;
        while((msg = in.readLine())!=null){
        	System.out.println(msg +" \n ");
        }
	}
	/**
	 * 输出到文件
	 * @param in
	 * @throws IOException
	 */
	private void outputWrite(BufferedReader in) throws IOException {
		File f = new File(file);
		if(! f.exists()) {
			f.createNewFile();
		}
		System.out.println(f.getAbsolutePath());
		FileWriter out = new FileWriter(f, true);
		String msg = null;
        while((msg = in.readLine())!=null){
        	out.write(msg+"\n");
        	out.flush();
        }
        out.close();
	}

}
