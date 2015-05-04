package svd.bgd;

import svd.SvdModel;

public class Model extends SvdModel{
	
	
	double[][] userdiffs;
	double[][] itemdiffs;
	double[][] ratingMatrix;
	
	public Model(int un, int in, int r, double minPref, double maxPref,double[][] ratings) {
		super(un, in, r, minPref, maxPref);
		
		userdiffs = new double[userNum][rank];
		itemdiffs = new double[itemNum][rank];
		ratingMatrix= ratings;

	}
	
	public void clearDiffs()
	{
		for(int u=0;u<userNum;u++)
		{
			for(int r=0;r<rank;r++)
			{
				userdiffs[u][r]=0;//-k*userVectors[u][r];
			}			
		}
		
		for(int i=0;i<itemNum;i++)
		{
			for(int r=0;r<rank;r++)
			{
				itemdiffs[i][r]=0;//-k*itemVectors[i][r];
			}			
		}
		
	}
	
	public double update()
	{
		clearDiffs();
		double sum=0;
		int count=0;
		//user特征和item特征偏导数
		for(int u=0;u<userNum;u++)
		{
				for(int i=0;i<itemNum;i++)
				{
					
					if(ratingMatrix[u][i]==0)
					{
					   //do nothing
						/*
						for(int r=0;r<rank;r++)
						{
							userdiffs[u][r]+= 0-k*userVectors[u][r];
							itemdiffs[i][r]+= 0-k*itemVectors[i][r];
						}	
						*/
						//看论文这部分是需要的,但是这样子实验结果不对.去掉这部分反而对了
						//论文后面有改进,明确了对未知评分不作处理.可以看公式16
						//没有绝对的对错,只有优劣之分
					}
					else
					{
						double predicted=predict(u,i);
						double error=ratingMatrix[u][i]-predicted;
						sum=sum+error*error;
						count++;
						for(int r=0;r<rank;r++)
						{
							userdiffs[u][r]+= (error*itemVectors[i][r]-k*userVectors[u][r]);//
							itemdiffs[i][r]+= (error*userVectors[u][r]-k*itemVectors[i][r]);//
						}	
						//map-reduce实现思路,先对每个user-item作map处理,算出部分偏导数,然后user作为key得到user的偏导数,item作为key得到item的偏导数
					}
				}			
		}
				
		//更新特征
		for(int u=0;u<userNum;u++)
		{		
			for(int r=0;r<rank;r++)
			{
				userVectors[u][r]=userVectors[u][r]+userdiffs[u][r]*alpha;
			}			
		}
		
		//更新特征
		for(int i=0;i<itemNum;i++)
		{
			for(int r=0;r<rank;r++)
			{
				itemVectors[i][r] = itemVectors[i][r]+itemdiffs[i][r]*alpha;
			}
		}
		
		double rmse=Math.sqrt(sum/count);
		return rmse;
	}
	


}
