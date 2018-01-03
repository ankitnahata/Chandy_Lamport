import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;
public class ClientHandler implements Runnable
{
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	int a=0,pid;  
	Processtable pt = new Processtable();
	
	public ClientHandler(Socket s,DataInputStream dis, DataOutputStream dos,Processtable ppt,int pid) 
	{
		this.s = s;
		this.pt = ppt;
		this.pid=pid;
		this.dis = dis;
		this.dos=dos;
	}
	
	public void  run() 
	{
		
		try {
				dos.writeInt(pid);
				//int port = dis.readInt();
				InetAddress addr = s.getInetAddress();
				int port = s.getPort();
				System.out.print(" Addr : " + addr + " port : " + port + "\n\n");
				pt.putinfo(pid, addr, port);						
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		try
		{
			// closing resources
			dis.close();
			dos.close();
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}



