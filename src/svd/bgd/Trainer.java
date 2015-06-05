package svd.bgd;

import java.io.IOException;

import rating.RatingReader;
import rating.RatingRecord;
import svd.MovieLensDataReader;



public class Trainer {

	Model model;
	RatingReader trainData;
	String trainFile;
	
	
	int userNum;
	int itemNum;
	
	public Trainer(String trainFilePath) throws IOException
	{
		trainFile = trainFilePath;
		trainData = new MovieLensDataReader(trainFile);
		userNum = trainData.getUserNum();
		itemNum = trainData.getItemNum();
		double minPref=trainData.getMinPref();
		double maxPref=trainData.getMaxPref();
		
		int rank =150;
		
		double[][] ratingMatrix = new double[userNum][itemNum];
		
		for(int u=0;u<userNum;u++)
		{
			for(int i=0;i<itemNum;i++)
			{
				ratingMatrix[u][i]=0;
			}
		}
		
		while(trainData.hasNext())
		{
			RatingRecord rating = trainData.getNext();
			int u=rating.getUser();
			int i=rating.getItem();
			double p=rating.getPref();
			ratingMatrix[u][i]=p;			
		}
		
		model= new Model(userNum,itemNum,rank,minPref,maxPref,ratingMatrix);
		

				
	}
	
	public void train(int maxIter)
	{
		double lastRmse = Double.MAX_VALUE;
		for(int iter=0;iter<maxIter;iter++)
		{		
			
			double rmse=model.update();	
			if(lastRmse<rmse) break;				
			lastRmse= rmse;
			System.out.println("iter:"+iter+", rmse:"+rmse);
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
