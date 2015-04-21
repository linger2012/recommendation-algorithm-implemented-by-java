package svd;

public abstract class SvdReader {
	public abstract int getUserNum();
	public abstract int getItemNum();
	public abstract double getMinPref();
	public abstract double getMaxPref();
	
	public abstract boolean hasNext();	
	public abstract RatingRecord getNext();
	public abstract void reset();
}
