package personalrank;



class Item implements Comparable<Item>
{
	int id;
	float weight;
	public Item(int id,float weight)
	{
		this.id=id;
		this.weight=weight;
	}
	


	@Override
	public int compareTo(Item o) {
		if(weight<o.weight)
		{
			return 1;
		}
		else if(weight>o.weight)
		{
			return -1;
		}
		else return 0;
	}
	
}
