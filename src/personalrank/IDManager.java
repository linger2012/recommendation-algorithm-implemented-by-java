package personalrank;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import rating.RatingRecord;

public class IDManager {

	
	
	private HashMap<Integer,Integer> user2Internal;
	private int userMaxIternalID;
	private HashMap<Integer,Integer> item2Internal;
	private int itemMaxIternalID;
	private int[] internalMapping;
	
	public int[] getInternalMapping()
	{
		return internalMapping;
	}
	
	public HashMap<Integer,Integer> getAllUser()
	{
		return user2Internal;
	}
	
	public int totalUserItem()
	{
		return itemMaxIternalID+1;
	}
	
	public int getUserMaxIternalID()
	{
		return userMaxIternalID;
	}
	
	public boolean isUser(int internalID)
	{
		return internalID<=userMaxIternalID;
	}
	
	public boolean isItem(int internalID)
	{
		return internalID>userMaxIternalID;
	}
	
	public int getOriginID(int internalID)
	{
		if(internalID<internalMapping.length)
			return internalMapping[internalID];
		return -1;
	}
	
	public int getUserInternalID(int user)
	{
		if(user2Internal.containsKey(user))
			return user2Internal.get(user);
		return -1;
	}
	
	public int getItemInternalID(int item)
	{
		if(item2Internal.containsKey(item))
			return item2Internal.get(item);
		return -1;
	}
	
	public IDManager(ListenReader reader)
	{
		user2Internal = new HashMap<Integer,Integer> ();
		item2Internal = new HashMap<Integer,Integer> ();
		
		reader.reset();
		int count=0;
		while(reader.hasNext())
		{
			RatingRecord record = reader.getNext();
			int u = record.getUser();
			if(!user2Internal.containsKey(u))
			{
				user2Internal.put(u, count++);
			}
		}
		userMaxIternalID=count-1;
		
		reader.reset();
		while(reader.hasNext())
		{
			RatingRecord record = reader.getNext();
			int i = record.getItem();
			if(!item2Internal.containsKey(i))
			{
				item2Internal.put(i, count++);
			}
		}
		
		assert(user2Internal.size()+item2Internal.size()==count);
		itemMaxIternalID = count-1;
		
		internalMapping = new int[count];
		
		Iterator<Entry<Integer, Integer>>  iter = user2Internal.entrySet().iterator();
		Entry<Integer, Integer> entry;
		while(iter.hasNext())
		{
			entry = iter.next();
			internalMapping[entry.getValue()]=entry.getKey();
		}
		
		iter = item2Internal.entrySet().iterator();
		while(iter.hasNext())
		{
			entry = iter.next();
			internalMapping[entry.getValue()]=entry.getKey();
		}
		
		
	}
	


}
