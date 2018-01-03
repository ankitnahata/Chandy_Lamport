import java.net.*;
import java.util.Random;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class Process {
	public static void main(String[] args) throws Exception
	{	
		int procnum=0,temp=0;
		if(args.length >= 1 && args[0].equals("-c"))
			{
				String pid=null;
				int interval=0;
				int terminate=0;
				int[][] nb = new int[100][100];
				//System.out.println(pid + " " + procnum + " " +interval + " " +terminate);
		 		File file = new File("E:/Usr/UTD_AdvancedOS/dsConfig");
		 		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		 		String line = null;
		 		int i=0;
		 		while( (line = br.readLine())!= null )
		 		{
		        // \\s+ means any number of whitespaces between tokens
		 			String [] tokens = line.split("\\s+");
		 			if(i==0)
		 			{
		 				pid=tokens[1];
		 			}	
		 			else if(i==1)
		 			{
		 				procnum=Integer.parseInt(tokens[3]);
		 			}
		 			else if(i==2)
		 			{
		 				interval=Integer.parseInt(tokens[1]);		        	
		 			}
		 			else if(i==3)
		 			{
		 				terminate=Integer.parseInt(tokens[1]);
		 			}	
		 			else if(i==4)
		 			{}
		 			else if((i>=5)&&(i<(5+procnum)))	
		 			{
		 				int tempj=0;
		 				while(tempj<tokens.length)
		 				{
		 					nb[i-5][tempj] = Integer.parseInt(tokens[tempj]);
		 					tempj++;
		 				}
		 			}
		 			i++;
		 			
		 		}
		 		for(int tempi=0;tempi<procnum;tempi++)
	 			{
	 				for(int tempj=0;tempj<procnum;tempj++)
		 			{
	 					System.out.print(nb[tempi][tempj]);
		 			}
	 				System.out.println("");
	 			}
		
		
		ServerSocket ss = new ServerSocket(5056);
		
		int threads=0;
		Processtable pt = new Processtable();
		pt.putinfo(1, InetAddress.getByName("localhost"), 5056);
		while (threads<procnum-1) 
		{
			Socket s = null;
			
			try 
			{
				s = ss.accept();
				System.out.println("Register Message Received : " + s);
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				Thread t = new Thread(new ClientHandler(s,dis,dos,pt,threads+2));
				t.start();
				threads++;
			}
			catch (Exception e){
				s.close();
				e.printStackTrace();
			}
		}
			TimeUnit.SECONDS.sleep(1);
			System.out.println("\n");
			NBInfo[] nblist = new NBInfo[procnum+1];
			
			for(int ii=1;ii<=procnum;ii++)
			{
				System.out.println(ii + " :  " + pt.getport(ii) + " : " + pt.gethostname(ii));
			}
			
			for(int ii=0;ii<procnum;ii++)
			{
				nblist[nb[ii][0]]=new NBInfo();
				for(int jj=1;jj<=procnum;jj++)
				{
					if(nb[ii][jj]!=0)
					{
						nblist[nb[ii][0]].putinfo(jj, nb[ii][jj], pt.gethostname(nb[ii][jj]), pt.getport(nb[ii][jj]));
					}
				}
			}
			
			for(int ii=1;ii<=procnum;ii++)
			{
				System.out.println(ii + " :-");
				for(int jo=1;jo<=nblist[ii].num;jo++)
				{
					System.out.println(nblist[ii].npid[jo] + " " + nblist[ii].neighbours[jo] + " " + nblist[ii].ports[jo]);
				}
			}
			
			for(int ii=2;ii<=procnum;ii++)
			{
				Socket serv = new Socket(pt.gethostname(ii),pt.getport(ii));
				ObjectOutputStream oos = new ObjectOutputStream(serv.getOutputStream());
				oos.flush();
				Thread t = new Thread(new ServerFirst(serv,ii,nblist,oos));
				t.start();					
			}
			int first=0;
			
			for(int ii=1;ii<=nblist[1].num;ii++)
			{
				Socket sss = new Socket(nblist[1].getinetadd(ii),nblist[1].getport(ii));
				DataOutputStream dss = new DataOutputStream(sss.getOutputStream());
				Thread th = new Thread(new ProcessComm(sss,dss,1,first));
			    th.start();
			}
			RetInt r = new RetInt();
			first++;
			int nbcount =0,retval=1;;
			while(true)
			{
				Socket servp = ss.accept();
				if(nbcount<nblist[1].num)
				{
				System.out.println("New Connection accepted : " + servp);	
				ProcessListener tt2 = new ProcessListener(servp,1);
				r=tt2.call();
				nbcount++;
				}
				else
				{
					Thread tt3 = new Thread(new ServerReady(servp,retval));
					tt3.start();
					retval++;
					if(retval==procnum)
					{
						System.out.println("\nReady Received From all");
						for(int ii=1;ii<=nblist[1].num;ii++)
						{
							Socket sss = new Socket(nblist[1].getinetadd(ii),nblist[1].getport(ii));
							DataOutputStream dss = new DataOutputStream(sss.getOutputStream());
							Thread th2 = new Thread(new ProcessComm(sss,dss,1,first));
							th2.start();
						}break;
					}
				}
			 }
			
			int[] arr={1,2,3,4,5};
			
			
			int[] SENT = new int[procnum];
			int[] RECV = new int[procnum+1];
			int[] CHANNEL = new int[100];
			int temptime=0;
			int mark=0;	
			int tempnum=0;
			
			
			while(true)
			{
			Random gen = new Random();
			int num = gen.nextInt(nblist[1].num+1);
			if(num==0)
				num++;								
			Socket so = new Socket(nblist[1].getinetadd(num),nblist[1].getport(num));
			DataOutputStream dss = new DataOutputStream(so.getOutputStream());
			Thread th = new Thread(new ProcessComm(so,dss,1,1));
			th.start();
			SENT[num]++;
			
			Socket servp = ss.accept();	
			ProcessListener tt2 = new ProcessListener(servp,1);
			r=tt2.call();
			
			Random gen2 = new Random();
			int rnd = gen2.nextInt(4);
			temptime = temptime + rnd;
			TimeUnit.SECONDS.sleep(arr[rnd]);
			
			if(temptime > 10 && mark==0)
			{
				int tempii;	
				System.out.println("\nReceived arr is : ");
				for(tempii=1;tempii<=procnum;tempii++)
				{
					System.out.print(RECV[tempii]+ " , ");
					
				}
				
				System.out.println("\nSent arr is : ");
				for(tempii=1;tempii<=nblist[1].num;tempii++)
				{				
					System.out.print(SENT[tempii]+" < ");
				}
				
				for(int ii=1;ii<=nblist[1].num;ii++)
				{
					Socket smark = new Socket(nblist[1].getinetadd(ii),nblist[1].getport(ii));
					DataOutputStream dmark = new DataOutputStream(smark.getOutputStream());
					Thread tmark = new Thread(new ProcessComm(smark,dmark,1,-1));
				    tmark.start();
				}
				temptime=0;
				mark=1;
				
			}
			if(r.val==-1)
			{
				while(true)
				{	
				System.out.println("\n\nMarker Received");
				int tempii;
				if(mark==1)
				{
					if (tempnum<nblist[1].num-1)
					{
					System.out.println("\nChannel arr is : ");
					for(tempii=1;tempii<=nblist[1].num;tempii++)
					{				
						System.out.print(CHANNEL[tempii]+" < ");
					}
					tempnum++;
					break;
					}
					System.out.println("\nChannel arr is : ");
					for(tempii=1;tempii<=nblist[1].num;tempii++)
					{				
						System.out.print(CHANNEL[tempii]+" < ");
					}
					tempnum=0;
					mark=0;
					System.out.println("Mark = 0 and Loop Cleaning ");
					for(tempii=1;tempii<=nblist[1].num;tempii++)
					{				
						CHANNEL[tempii]=0;
					}
					
				}
				break;
				}
			}				
			else
			{
			int tempii;			
			for(tempii=1;tempii<=nblist[1].num;tempii++)
			{				
				if(nblist[1].npid[tempii]==r.val)
				{
					break;
				}
			}
			RECV[tempii]++;
			if(mark==1)
			{
				CHANNEL[tempii]++;
			}
			
			}
			
			}
						
						
			
		}
		else
		{
			try
			{
				
				InetAddress ip = InetAddress.getByName("localhost");
				Socket s = new Socket(ip, 5056);
				s.setReuseAddress(true);
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				int myid = dis.readInt();
				System.out.println("The Process ID assigned is : " + myid);
				System.out.println(s.getLocalPort());
				s.close();
				NBInfo[] nbm = null;
				ServerSocket ss = new ServerSocket(s.getLocalPort());
				int tempi=0;
				int a=0;
				
				RetInt r = new RetInt();
				while(true)
				{
				Socket servs = null,servp=null;
				try 
				{
					
					if(tempi==0)
					{
						servs = ss.accept();
						ProcessHandler tt = new ProcessHandler(servs,myid);
						nbm = tt.call();
						for(int ii=1;ii<=nbm[myid].num;ii++)
						{
							Socket sss = new Socket(nbm[myid].getinetadd(ii),nbm[myid].getport(ii));
							DataOutputStream dss = new DataOutputStream(sss.getOutputStream());
							Thread th = new Thread(new ProcessComm(sss,dss,myid,tempi));
							th.start();
						}
						tempi=1;
					}
				
					else
					{
						servp = ss.accept();
						System.out.println("New Connection accepted : " + servp);	
						ProcessListener tt2 = new ProcessListener(servp,myid);
						r=tt2.call();
						a++;
						if(a==nbm[myid].num)
						{
							Socket servtemp = new Socket(ip, 5056);
							DataOutputStream doss = new DataOutputStream(servtemp.getOutputStream());
							doss.writeUTF("Ready");
							break;
						}
					}
					
				 }
				
				 catch (Exception e)
				 {
					s.close();
					e.printStackTrace();
				 }						
				 }
				
				int[] arr={1,2,3,4,5};
				int[] SENT = new int[100];
				int[] RECV = new int[100];
				int[] CHANNEL = new int[100];
				int mark=0;
				int tempnum=0;
				while(true)
				{
				Random gen = new Random();
				int num = gen.nextInt(nbm[myid].num+1);
				if(num==0)
					num++;								
				Socket sss = new Socket(nbm[myid].getinetadd(num),nbm[myid].getport(num));
				DataOutputStream dss = new DataOutputStream(sss.getOutputStream());
				Thread th = new Thread(new ProcessComm(sss,dss,myid,tempi));
				th.start();
				SENT[num]++;
				
				Socket servp = ss.accept();	
				ProcessListener tt2 = new ProcessListener(servp,myid);
				
				Random gen2 = new Random();
				int rnd = gen2.nextInt(4);
				r=tt2.call();
				TimeUnit.SECONDS.sleep(arr[rnd]);
				String fname = "localstate_" + Integer.toString(myid);
				
				
				if(r.val==-1)
				{
					while(true)
					{	
					System.out.println("\n\nMarker Received");
					int tempii;
					if(mark==1)
					{
						if (tempnum<nbm[myid].num-1)
						{
						System.out.println("\nChannel arr is : ");
						for(tempii=1;tempii<=nbm[myid].num;tempii++)
						{				
							System.out.print(CHANNEL[tempii]+" < ");
						}
						tempnum++;
						break;
						}
						
						System.out.println("\nChannel arr is : ");
						for(tempii=1;tempii<=nbm[myid].num;tempii++)
						{				
							System.out.print(CHANNEL[tempii]+" < ");
						}
						tempnum=0;
						mark=0;
						for(tempii=1;tempii<=nbm[myid].num;tempii++)
						{				
							CHANNEL[tempii]=0;
						}
						System.out.println("Mark = 0 and Channel cleansed \n");
						
					}
					System.out.println("\n" + fname);
					System.out.println("Received Array is : ");
					
					for(tempii=1;tempii<=nbm[myid].num;tempii++)
					{
						System.out.print(RECV[tempii]+ " , ");
						
					}
					
					System.out.println("\nSent arr is : ");
					for(tempii=1;tempii<=nbm[myid].num;tempii++)
					{				
						System.out.print(SENT[tempii]+" < ");
					}
								
					System.out.println("\n\n");
					for(int ii=1;ii<=nbm[myid].num;ii++)
					{
						Socket smark = new Socket(nbm[myid].getinetadd(ii),nbm[myid].getport(ii));
						DataOutputStream dmark = new DataOutputStream(smark.getOutputStream());
						Thread tmark = new Thread(new ProcessComm(smark,dmark,myid,-1));
						tmark.start();
					}
					mark=1;
					break;
					}
				}
				
				else
				{
					int tempii;
					for(tempii=1;tempii<=nbm[myid].num;tempii++)
					{				
						if(nbm[myid].npid[tempii]==r.val)
						{
							break;
						}
					}
					RECV[tempii]++;
					if(mark==1)
					{
						CHANNEL[tempii]++;
					}
				}
							
				
				}
								
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}


