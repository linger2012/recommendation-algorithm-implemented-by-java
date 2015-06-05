package rating;


public abstract class RatingReader {
	public abstract int getUserNum();
	public abstract int getItemNum();
	public abstract double getMinPref();
	public abstract double getMaxPref();
	
	public abstract boolean hasNext();	
	public abstract RatingRecord getNext();
	public abstract void reset();
}
