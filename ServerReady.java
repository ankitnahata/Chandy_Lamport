import java.net.Socket;
public class ServerReady implements Runnable{
	final Socket s;
	int processnum;
	public ServerReady(Socket s,int processnum) 
	{
		this.s = s;
		this.processnum = processnum;
	}
	
	public void  run() 
	{
		
		try {
				System.out.println("Ready Received from Process : " + processnum);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
					
	}
}
