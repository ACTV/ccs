package actv.ccs.model.ui;
public interface IObservable
{ 
	public void addObserver(IObserver o);
	public void notifyObservers();
}