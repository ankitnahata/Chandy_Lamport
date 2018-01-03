import java.net.*;
import java.io.Serializable;
public class NBInfo implements Serializable {
	int[] npid = new int[10];
	int num;
	InetAddress[] neighbours = new InetAddress[10];
	int[] ports= new int[10];
	public void putinfo(int num, int ppid, InetAddress nb, int pport)
	{
		this.num=num;
		this.npid[num]=ppid;
		this.neighbours[num]=nb;
		this.ports[num]=pport;
		
	}
	public InetAddress getinetadd(int num)
	{		
		return neighbours[num];
	}
	public int getport(int num)
	{		
		return ports[num];
	}
}
