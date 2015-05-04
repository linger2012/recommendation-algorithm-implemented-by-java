package svd;

public class SvdModel {
	
	
	protected double alpha=0.001;
	protected double k=0.02;
	
	protected int userNum;
	protected int itemNum;
	protected int rank;

	protected double minPref;
	protected double maxPref;
	
	protected double[][] userVectors;
	protected double[][] itemVectors;
	

	
	public SvdModel(int un,int in,int r,double minPref,double maxPref)
	{
		
		userNum=un;
		itemNum=in;
		rank=r;
		this.minPref=minPref;
		this.maxPref=maxPref;
		

		userVectors = new double[userNum][rank];
		itemVectors = new double[itemNum][rank];


		
		for(int u=0;u<userNum;u++)
		{
			for(int i=0;i<rank;i++)
			{
				userVectors[u][i] = Math.random()*(Math.sqrt((maxPref-minPref)/rank));
				//调整了初始化的方式,也对性能有提升
			}
		}
		
		for(int u=0;u<itemNum;u++)
		{
			for(int i=0;i<rank;i++)
			{
				itemVectors[u][i] = Math.random()*(Math.sqrt((maxPref-minPref)/rank));
			}
		}		
	}
	
	public double predict(int u,int i)
	{
		double score=0.0;
		for(int r=0;r<rank;r++)
		{
			score= score+userVectors[u][r]*itemVectors[i][r];
		}
		
		if(score>(maxPref-minPref))
		{
			score = maxPref;
		}
		else if(score<0)
		{
			score = minPref;
		}
		else
		{
			score +=minPref;
		}
		
		return score;
	}
	

	
	
	
}
