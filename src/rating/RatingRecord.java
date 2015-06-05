package rating;

public class RatingRecord {
	
	int user;
	int item;
	double pref;
	
	
	public RatingRecord(int u,int i,double p)
	{
		user=u;
		item=i;
		pref=p;
	}
	
	public int getUser()
	{
		return user;
	}
	public int getItem()
	{
		return item;
	}
	public double getPref()
	{
		return pref;
	}

	
}
