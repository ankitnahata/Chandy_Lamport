import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
public class ProcessHandler implements Callable{
	final Socket s;
	int a=0,pid;  
	Processtable pt = new Processtable();
	ObjectInputStream ois;
	NBInfo[] nb;
	public ProcessHandler(Socket s,int myid) 
	{
		this.s = s;
		this.pid = myid;
	}
	
	public NBInfo[] call() throws Exception
	{
		
		try {
				ObjectInputStream ooois = new ObjectInputStream(s.getInputStream());
				nb = (NBInfo[])ooois.readObject();
								
			} catch (Exception e) {
				e.printStackTrace();
			}
		return nb;	
	}
}
