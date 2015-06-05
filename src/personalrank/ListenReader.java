package personalrank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import rating.RatingReader;
import rating.RatingRecord;

public class ListenReader extends RatingReader{

	LinkedList<RatingRecord> records;
	Iterator<RatingRecord> iter;
	BufferedReader br;
	public ListenReader(String filePath) throws IOException
	{
		records = new LinkedList<RatingRecord>();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
		String line;
		while(true)
		{
			line=br.readLine();
			if(line==null) break;
			String[] fields=line.split(",");
			int u= Integer.parseInt(fields[0]);
			int i= Integer.parseInt(fields[1]);
			records.push(new RatingRecord(u,i,1));				
		}
		br.close();
		
	}

	


	@Override
	public double getMinPref() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double getMaxPref() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return iter.hasNext();
	}

	@Override
	public RatingRecord getNext() {
		// TODO Auto-generated method stub
		return iter.next();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		iter = records.iterator();
	}
	
	//以下两个方法是无效方法
	@Override
	public int getUserNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemNum() {
		// TODO Auto-generated method stub
		return 0;
	}

}
