package com.automic.objects;

import java.io.IOException;

import com.automic.rules.IASimpleRule;
import com.uc4.api.Template;
import com.uc4.api.objects.IFolder;
import com.uc4.api.objects.RuleEvent;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class Rules extends ObjectTemplate{

	public Rules(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}
	
	// should contain stuff for Just Rule Events for now..
	public RuleEvent getTimeEventFromObject(UC4Object object){return (RuleEvent) object;}
	
	public void createRuleEvent(String EventName, IFolder FolderLocation) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		broker.common.createObject(EventName, Template.EVNT_IA_EVENT_RULE, FolderLocation);
	}
	
	public void setExecutableAlias(RuleEvent evnt, String alias) {
		evnt.settings().getEventKeyValues().replace("EXECUTABLE_ALIAS", alias);
	}
	
	public void setEventDefinition(RuleEvent evnt, String EventDefName) {
		evnt.settings().getEventKeyValues().replace("EVENT_DEFINITION_REF", EventDefName);
	}
	
	public void setEventExecutable(RuleEvent evnt, String ExecutableName) {
		evnt.settings().getEventKeyValues().replace("EXECUTABLE_OBJECT_NAME_REF", ExecutableName);
	}
	
	public String getEventDefinition(RuleEvent evnt) {
		return evnt.settings().getEventKeyValues().get("EVENT_DEFINITION_REF");
	}
	
	public String getEventExecutable(RuleEvent evnt) {
		return evnt.settings().getEventKeyValues().get("EXECUTABLE_OBJECT_NAME_REF");
	}
	
	public String getExecutableAlias(RuleEvent evnt) {
		return evnt.settings().getEventKeyValues().get("EXECUTABLE_ALIAS");
	}
	
	public void showConditions(RuleEvent evnt) {
		IASimpleRule rule = new IASimpleRule(evnt);
		rule.showConditions();
	}
}
