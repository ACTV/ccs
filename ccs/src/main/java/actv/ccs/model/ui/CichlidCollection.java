package actv.ccs.model.ui;

import java.util.Vector;

import actv.ccs.model.objects.*;

public class CichlidCollection implements Iterable
{
	private Vector<ConvictCichlid> cichlidCollection; 

	public CichlidCollection()
	{
		cichlidCollection = new Vector<ConvictCichlid>();
	}
	public void add(ConvictCichlid obj)
	{
		cichlidCollection.add(obj);
	}
	public int size()
	{
		return cichlidCollection.size();
	}
	public void removeObject(ConvictCichlid o)
	{
		cichlidCollection.remove(o);
	}
	public Iterator getIterator()
	{
		return new CichlidIterator();
	}
	private class CichlidIterator implements Iterator 
	{
		private int index;
		
		public CichlidIterator()
		{
			index = -1;
		}
		public boolean hasNext()
		{
			if (cichlidCollection.size() <= 0)
			{
				return false;
			}
			if (index == cichlidCollection.size() - 1)
			{
				return false;
			}
			return true;
		}
		public ConvictCichlid getNext()
		{
			index++;
			return cichlidCollection.elementAt(index);
		}
		public void remove()
		{
			if (index > -1)
			//	cichlidCollection.remove(index);
				cichlidCollection.removeElementAt(index);
		}

	}
}
