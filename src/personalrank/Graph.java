package personalrank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import rating.RatingRecord;

public class Graph {


	
	ArrayList<HashMap<Integer,Float> > graph;
	
	IDManager idManager;
	public Graph(ListenReader reader)
	{
		idManager = new IDManager(reader);
		
		int userItemNum = idManager.totalUserItem();
		
		graph = new ArrayList<HashMap<Integer,Float> >();
		for(int i=0;i<userItemNum;i++)
		{
			graph.add(new HashMap<Integer,Float>());//申请空间
		}
		
		reader.reset();
		RatingRecord record;
		int u;
		int i;
		float p;
		while(reader.hasNext())
		{
			record = reader.getNext();
			u = idManager.getUserInternalID(record.getUser());
			i = idManager.getItemInternalID(record.getItem());
			p = (float) record.getPref();
			graph.get(u).put(i, p);
			graph.get(i).put(u, p);			
		}
		
	}
	
	
	public void recommend(int start,int end)//为了多线程调用
	{
		float alpha = (float) 0.8;
		int maxIters = 20;
		int k = 100;
		
		int[] internalMapping = idManager.getInternalMapping();
		int userMaxIternalID = idManager.getUserMaxIternalID();
		
		if(start>userMaxIternalID || end>userMaxIternalID) return;
		int originID;
		int internalID;
		for(int i=start;i<end;i++)
		{
			originID = internalMapping[i];
			internalID = i;			
			ArrayList<Item> sorted = personalRank(internalID, alpha, maxIters);
			
			for(int j=0;j<k;j++)
			{
				Item item = sorted.get(j);
				int itemID = idManager.getOriginID(item.id);
				float w = item.weight;
			}
		}
		
		
		
	}
	
	public void recommend()
	{
		HashMap<Integer,Integer> user2Internal = idManager.getAllUser();
		
		System.out.println(user2Internal.size());//10449
		//已推荐10400位
	//	总耗时为：2544秒
		
		float alpha = (float) 0.8;
		int maxIters = 20;
		int k = 100;
		
		Iterator<Entry<Integer, Integer>> iter = user2Internal.entrySet().iterator();
		int originID;
		int internalID;
		Entry<Integer, Integer> entry;
		long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
		int count=0;
		while(iter.hasNext())
		{
			entry = iter.next();
			originID = entry.getKey();
			internalID = entry.getValue();			
			ArrayList<Item> sorted = personalRank(internalID, alpha, maxIters);
			
			for(int i=0;i<k;i++)
			{
				Item item = sorted.get(i);
				int itemID = idManager.getOriginID(item.id);
				float w = item.weight;
				//System.out.println(itemID+":"+w);
			}
			
			count++;
			
			if(count%100==0)
			{
				long endMili=System.currentTimeMillis();	
				System.out.println("已推荐"+count+"位");
				System.out.println("总耗时为："+(endMili-startMili)/1000+"秒");
			}

			
		}
		
		
		
	}
	
	
	public void personalRank(int userOriginID)
	{
		int userInternalID = idManager.getUserInternalID(userOriginID);
		float alpha = (float) 0.8;
		ArrayList<Item> sorted = personalRank(userInternalID, alpha, 20);
		
		int k = 100;
		for(int i=0;i<k;i++)
		{
			Item item = sorted.get(i);
			int itemID = idManager.getOriginID(item.id);
			float w = item.weight;
			System.out.println(itemID+":"+w);
		}
		
	}
	
	public ArrayList<Item> personalRank(int userInternalID,float alpha,int maxIters)
	{
		int userItemNum = idManager.totalUserItem();
		float[] rank = new float[userItemNum]; 
		for(int i=0;i<userItemNum;i++)
		{
			rank[i]=0;
		}
		rank[userInternalID]=1;
		
		float[] rankCache;
		HashMap<Integer, Float>  edges;
		Iterator<Entry<Integer, Float>> edgeIter;
		Entry<Integer, Float> edge;
		int j;
		for(int iter=0;iter<maxIters;iter++)
		{		
			rankCache=new float[userItemNum];
			for(int i=0;i<userItemNum;i++)
			{
				rankCache[i]=0;
			}
			
			for(int i=0;i<graph.size();i++)
			{
				edges = graph.get(i);
				edgeIter = edges.entrySet().iterator();
				while(edgeIter.hasNext())
				{
					edge = edgeIter.next();
					j = edge.getKey();
					rankCache[j] += alpha*rank[i]/(edges.size()*1.0);
					if(j==userInternalID)
					{
						rankCache[j] += 1.0-alpha;
					}
					
				}
				
			}
			
			rank = rankCache;
			
		}
		
		ArrayList<Item> candidates = new ArrayList<Item>();
		//过滤+排序
		for(int i=idManager.getUserMaxIternalID()+1;i<rank.length;i++)
		{
			candidates.add(new Item(i,rank[i]));
		}
		
		Collections.sort(candidates);
		
		return candidates;
	
		
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ListenReader reader = new ListenReader("dataset/user_music.dat");
		Graph graph = new Graph(reader);
		
		
		
		//graph.recommend();//大概1秒4个,也就是0.25秒一个
		
		long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
		for(int i=0;i<100;i++)
		{
			graph.personalRank(79057533);
		}
		
		long endMili=System.currentTimeMillis();	
		System.out.println("总耗时为："+(endMili-startMili)+"毫秒");


	}

}
