package actv.ccs;

import org.drools.event.ObjectInsertedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.ObjectUpdatedEvent;
import org.drools.event.WorkingMemoryEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCSListener implements WorkingMemoryEventListener {
	private static final Logger logger = LoggerFactory.getLogger(CCSListener.class);
	
	public void objectInserted(ObjectInsertedEvent event) {
		logger.info("Inserted {}", event.getObject().toString());
	}

	public void objectUpdated(ObjectUpdatedEvent event) {
		logger.info("Updated {}", event.getObject().toString());
	}

	public void objectRetracted(ObjectRetractedEvent event) {
		logger.info("Retracted {}", event.getOldObject().toString());
	}

}
