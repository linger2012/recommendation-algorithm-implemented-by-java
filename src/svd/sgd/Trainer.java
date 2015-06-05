package svd.sgd;

import java.io.IOException;

import rating.RatingReader;
import rating.RatingRecord;
import svd.MovieLensDataReader;

public class Trainer {	
	
	
	
	Model model;
	RatingReader trainData;
	String trainFile;
	
	public Trainer(String trainFilePath) throws IOException
	{
		trainFile = trainFilePath;
		trainData = new MovieLensDataReader(trainFile);
		int userNum = trainData.getUserNum();
		int itemNum = trainData.getItemNum();
		double minPref=trainData.getMinPref();
		double maxPref=trainData.getMaxPref();
		
		int rank =150;//增大潜在隐义因子数量,收敛速度较快,但程序运行速度较慢
		model= new Model(userNum,itemNum,rank,minPref,maxPref);
		
		
	}
	
	public void train(int maxIter)
	{
		int count=0;
		double lastRmse = Double.MAX_VALUE;
		while(true)
		{		
			System.out.printf("第%d次迭代:",count++);
			double loss=0.0;
			int n=0;
			while(trainData.hasNext())
			{
				RatingRecord rating = trainData.getNext();
				int u=rating.getUser();
				int i=rating.getItem();
				double p=rating.getPref();
							
				//更新用户u的特征向量			
				//更新物品i的特征向量
				loss+= model.update(u,i,p);
				n++;							
			}
			
			double rmse=Math.sqrt(loss/n);
			System.out.println("rmse:"+rmse);
			
			//判断一下,如果rmse开始增大,则停止迭代
			if(rmse>lastRmse) break;
			if(count>maxIter) break;
			
			lastRmse = rmse;			
			trainData.reset();						
		}
		
	}
	
	public void test(String testFile) throws IOException
	{
		RatingReader testData = new MovieLensDataReader(testFile);
		double sum=0.0;
		int n=0;
		while(testData.hasNext())
		{
			RatingRecord rating = testData.getNext();
			int u=rating.getUser();
			int i=rating.getItem();
			double p=rating.getPref();
			double predicted = model.predict(u, i);
			double error=(p-predicted)*(p-predicted);
			sum+=error;	
			n++;
		}
		double rmse=Math.sqrt(sum/n);
		System.out.println("test rmse:"+rmse);
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Trainer trainer = new Trainer("dataset/ua.base");
		trainer.train(1000);
		trainer.test("dataset/ua.test");

	}

}
