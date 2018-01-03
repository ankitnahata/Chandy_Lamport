import java.io.DataOutputStream;
import java.net.Socket;
public class ProcessComm implements Runnable{
	Socket sss;
	DataOutputStream dss;
	int a=0,pid,first=0;  
	public ProcessComm(Socket sss, DataOutputStream dss, int myid,int num)
	{
		this.dss = dss;
		this.sss = sss;
		this.pid = myid;
		this.first = num;
	}
	
	public void  run() 
	{
		
		try {
				if(first==0)
				{
					dss.writeUTF("Hello from " + pid);
				//	dss.flush();
				
				}
				if(first==-1)
				{
					dss.writeUTF("Marker");
				}
				if(first!=0)
				{
					dss.writeUTF("Compute from " +pid);
				//	dss.flush();
					
				}
				
								
			} catch (Exception e) {
				e.printStackTrace();
			}
					
	}


}
