package svd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import rating.RatingReader;
import rating.RatingRecord;

public class MovieLensDataReader extends RatingReader{
	
	
	LinkedList<RatingRecord> records;
	Iterator<RatingRecord> iter;
	BufferedReader br;
	int maxUser;
	int maxItem;
	double minPref;
	double maxPref;
	
	public MovieLensDataReader(String filePath) throws IOException
	{
		minPref = Double.MAX_VALUE;
		maxPref = Double.MIN_VALUE;
		
		maxUser=-1;
		maxItem=-1;
		records = new LinkedList<RatingRecord>();	
		
		br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
		String line;
		while(true)
		{
			line=br.readLine();
			if(line==null) break;
			String[] fields=line.split("\t");
			int u= Integer.parseInt(fields[0]) -1;//MovieLens数据集的id从1开始
			int i= Integer.parseInt(fields[1]) -1;
			double p= Double.parseDouble(fields[2]);
			records.push(new RatingRecord(u,i,p));	
			
			maxUser = maxUser<u?u:maxUser;
			maxItem = maxItem<i?i:maxItem;
			minPref = minPref<p?minPref:p;
			maxPref = maxPref<p?p:maxPref;
			
			
		}
		br.close();
		iter = records.iterator();
	}
	

	
	public  double getMinPref()
	{
		return minPref;
	}
	
	public  double getMaxPref()
	{
		return maxPref;
	}
	
	
	public int getUserNum()
	{
		return maxUser+1;
	}
	
	public int getItemNum()
	{
		return maxItem+1;
	}
	
	public boolean hasNext()
	{
		return iter.hasNext();
	}
	
	public RatingRecord getNext()
	{
		return iter.next();
	}
	
	public void reset()
	{
		iter = records.iterator();
	}
	
	
}
