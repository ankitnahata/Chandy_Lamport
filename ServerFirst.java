import java.io.*;
import java.net.Socket;
public class ServerFirst implements Runnable{
	final Socket s;
	int a=0,pid;	
	NBInfo[] nblist = new NBInfo[10];
	ObjectOutputStream oos;
	public ServerFirst(Socket s, int myid, NBInfo[] nblist,ObjectOutputStream oos) 
	{
		this.s = s;
		this.pid = myid;
		this.oos = oos;
		this.nblist = nblist;
	}
	
	public void  run() 
	{
		
		try {
				oos.writeObject(nblist);
				oos.close();
				System.out.println("\nObject Sent to Process");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
					
	}
}
