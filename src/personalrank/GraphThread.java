package personalrank;

import java.io.IOException;

public class GraphThread  extends Thread{

	Graph graph;
	int start;
	int end;
	public GraphThread(Graph graph,int start,int end)
	{
		this.graph = graph;
		this.start =start;
		this.end = end;
	}

	public void run()
	{
		graph.recommend(start, end);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		ListenReader reader = new ListenReader("dataset/user_music.dat");
		Graph graph = new Graph(reader);
		
		int totalUser = graph.idManager.getUserMaxIternalID() +1;
		
		int threadNum = 10;
		GraphThread[] threads = new GraphThread[threadNum];
		int start=-1;
		int end=-1;
		long start_time = System.currentTimeMillis(); 
		for(int i=0;i<threadNum;i++)
		{
			start = end+1; if(start>totalUser) break;
			end=start+totalUser/threadNum; if(end>totalUser) end = totalUser;
			System.out.println("thread:"+i);
			System.out.println("start:"+start);
			System.out.println("end:"+end);
			
			threads[i] = new GraphThread(graph,start,end);
			threads[i].start();		
			
		}
		
		for(int i=0;i<threadNum;i++)
		{
			if(threads[i] !=null)
				threads[i].join();
			System.out.println("finished:"+i);
		}
		
		long end_time = System.currentTimeMillis();  
        System.out.println("子线程执行时长：" + (end_time - start_time));  
	}

}
