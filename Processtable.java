import java.io.*;
import java.util.*;
import java.net.*;
public class Processtable {
	int[] pid = new int[100];
	int[] port = new int[100];
	InetAddress hname[] = new InetAddress[100];
	
	public void putinfo(int ppid, InetAddress hhname, int pport)
	{
		
		hname[ppid] = hhname;
		port[ppid]=pport;
			
	}
	public int getport(int ppid)
	{
		return port[ppid];
	}
	public InetAddress gethostname(int ppid)
	{
		return hname[ppid];
	}
}
