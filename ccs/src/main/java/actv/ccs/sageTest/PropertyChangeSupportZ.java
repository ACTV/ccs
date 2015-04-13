package actv.ccs.sageTest;

import java.beans.PropertyChangeListener;

public interface PropertyChangeSupportZ {

	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue);
}
