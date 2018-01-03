import java.io.DataInputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ProcessListener implements Callable {
	final Socket s;
	int a=0,pid;  
	Processtable pt = new Processtable();
	RetInt r = new RetInt();
	public ProcessListener(Socket ss,int myid) 
	{
		this.s = ss;
		this.pid = myid;
	}
	
	public RetInt call() throws Exception
	{
		try {
				DataInputStream ds = new DataInputStream(s.getInputStream());
				String input = ds.readUTF();
				String[] input2 = input.split(" ",3);
				//Incase the input message is Compute
				if(input2[0].equals("Compute"))
				{
					System.out.println("\n" + input);
					r.val=Integer.parseInt(input2[2]);
					return r;
				}
				
				//Incase the message is Marker
				else if(input.equals("Marker"))
				{
					r.val = -1;
					r.port = s.getPort();
					return r;
				}
				
				//Incase the Input Message is Ready
				else
				{
					System.out.println("\n"+ input);
					
				}
				
				
				
						
			} catch (Exception e) {
				e.printStackTrace();
			}
		r.val=0;
		return r;
					
	}

}
