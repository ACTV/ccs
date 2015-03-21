package actv.ccs;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;
import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
import org.drools.event.rule.RuleFlowGroupActivatedEvent;
import org.drools.event.rule.RuleFlowGroupDeactivatedEvent;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCSListener implements WorkingMemoryEventListener, ProcessEventListener, AgendaEventListener {
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

	public void beforeProcessStarted(ProcessStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void afterProcessStarted(ProcessStartedEvent event) {
		logger.info("Process Started {}", event.toString());
		
	}

	public void beforeProcessCompleted(ProcessCompletedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void afterProcessCompleted(ProcessCompletedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void afterVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void activationCreated(ActivationCreatedEvent event) {
		logger.info("Activated {}", event.getActivation().getRule());
	}

	public void activationCancelled(ActivationCancelledEvent event) {
		logger.info("Activication cancelled {}", event.getActivation().getRule());			
	}

	public void beforeActivationFired(BeforeActivationFiredEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void afterActivationFired(AfterActivationFiredEvent event) {
		logger.info("Fired {}", event.getActivation().getRule());		
	}

	public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void agendaGroupPushed(AgendaGroupPushedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		logger.info("Activated {}", event.toString());
	}

	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub
		
	}
}
