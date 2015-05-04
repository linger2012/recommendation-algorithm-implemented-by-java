package svd.sgd;

import svd.SvdModel;

public class Model extends SvdModel{

	public Model(int un, int in, int r, double minPref, double maxPref) {
		super(un, in, r, minPref, maxPref);
		// TODO Auto-generated constructor stub
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
			userVecotr[r] = userVectors[user][r]+error*itemVectors[item][r]*alpha;
			itemVectors[item][r] =  itemVectors[item][r] + error*userVectors[user][r]*alpha;
			*/
			
			//加上正则化后,避免过拟合.训练集rmse降低速度较慢,但测试集效果很好.
			userVecotr[r] = userVectors[user][r]+(error*itemVectors[item][r]-k*userVectors[user][r])*alpha;
			itemVectors[item][r] =  itemVectors[item][r] + (error*userVectors[user][r]-k*itemVectors[item][r])*alpha;
			
			userVectors[user][r] = userVecotr[r];
		}	
		
		return error*error;
	}

}
