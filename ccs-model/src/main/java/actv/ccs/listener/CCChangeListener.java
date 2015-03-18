package actv.ccs.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCChangeListener implements PropertyChangeListener {
	private static final Logger logger = LoggerFactory.getLogger(CCChangeListener.class);
	
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getPropertyName().equals("state")){
			logger.info(">>> State of the fish changed from [ {} ] to [ {} ]", evt.getOldValue().toString(), evt.getNewValue().toString());
		}
	}

}
