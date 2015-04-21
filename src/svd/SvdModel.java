package svd;

public class SvdModel {
	
	
	double alpha=0.001;
	double k=0.02;
	
	int userNum;
	int itemNum;
	int rank;

	double[][] userVectors;
	double[][] itemVectors;
	

	
	public SvdModel(int un,int in,int r,double minPref,double maxPref)
	{
		userNum=un;
		itemNum=in;
		rank=r;
		

		userVectors = new double[userNum][rank];
		itemVectors = new double[itemNum][rank];


		
		for(int u=0;u<userNum;u++)
		{
			for(int i=0;i<rank;i++)
			{
				userVectors[u][i] = Math.sqrt(minPref/rank)+Math.random()*(Math.sqrt(maxPref/rank)-Math.sqrt(minPref/rank));
				//调整了初始化的方式,也对性能有提升
			}
		}
		
		for(int u=0;u<itemNum;u++)
		{
			for(int i=0;i<rank;i++)
			{
				itemVectors[u][i] = Math.sqrt(minPref/rank)+Math.random()*(Math.sqrt(maxPref/rank)-Math.sqrt(minPref/rank));
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
		return score;
	}
	
	public double update(int user,int item,double pref)
	{
		double predicted = predict(user, item);
		double error= pref - predicted;
		
		double[] userVecotr = new double[rank];
		for(int r=0;r<rank;r++)
		{
			//没有正则化,训练集rmse很快降低,但是测试集效果还是不好,证实了过拟合.
			/*
			userVecotr[r] = userVectors[user][r]+(error*itemVectors[item][r]-k*userVectors[user][r])*alpha;
			itemVectors[item][r] =  itemVectors[item][r] + (error*userVectors[user][r]-k*itemVectors[item][r])*alpha;
			*/
			
			//加上正则化后,避免过拟合.训练集rmse降低速度较慢,但测试集效果很好.
			userVecotr[r] = userVectors[user][r]+(error*itemVectors[item][r]-k*userVectors[user][r])*alpha;
			itemVectors[item][r] =  itemVectors[item][r] + (error*userVectors[user][r]-k*itemVectors[item][r])*alpha;
			userVectors[user][r] = userVecotr[r];
		}	
		
		return error*error;
	}
	
	
	
}
