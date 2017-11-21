package com.automic.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.uc4.api.objects.RuleEvent;

public class IASimpleRule {

//	[0_VALUE:1]
//			[EXECUTABLE_ALIAS:OTHER_ALIAS]
//			[VALUE_MAPPING_0_event.count:MYCOUNT#]
//			[1_ATTRIBUTE:event.success]
//			[0_OPERATOR:>]
//			[VALUE_MAPPING_1_event.message:MYMESSAGE#]
//			[1_VALUE:true]
//			[0_ATTRIBUTE:event.count]
//			[1_OPERATOR:==]
//			[RULE_TYPE_VERSION:1]
//			[RULE_TYPE:SIMPLE]
//			[VALUE_MAPPING_2_event.success:MYSUCCESS1#]
//			[EXECUTABLE_OBJECT_NAME_REF:SCRI.SIMPLE.1]
//			[EVENT_DEFINITION_REF:SIMPLE.EVENT]
//			[KEY:]
	
		public RuleEvent event;
		public String RuleName = "";
		public String RuleType = "";
		public String RuleVersion = "";
		public String RuleKey = "";
		public String RuleSettingsVARA = "";
		public String RuleExecutableObjectName = "";
		public String RuleExecutableObjectAlias = "";
		
		public String RULE_TYPE_VERSION = "RULE_TYPE_VERSION";
		public String RULE_TYPE = "RULE_TYPE";
		public String RULE_KEY = "KEY";
		public String EXECUTABLE_OBJECT_NAME_REF = "EXECUTABLE_OBJECT_NAME_REF";
		public String EVENT_DEFINITION_REF = "EVENT_DEFINITION_REF";
		public String EXECUTABLE_ALIAS = "EXECUTABLE_ALIAS";
		
		public ArrayList<IACondition> Conditions = new ArrayList<IACondition>();
		public ArrayList<IAVarMapping> Mappings = new ArrayList<IAVarMapping>();
		
		
	public IASimpleRule(RuleEvent evnt) {
		
		this.event = evnt;
		this.RuleName = this.event.getName();
		Map<String,String> ObjMap = this.event.settings().getEventKeyValues();
		Set<String> Keys = this.event.settings().getEventKeyValues().keySet();

		this.RuleType = ObjMap.get(RULE_TYPE);
		this.RuleVersion = ObjMap.get(RULE_TYPE_VERSION);
		this.RuleKey = ObjMap.get(RULE_KEY);
		this.RuleSettingsVARA = ObjMap.get(EVENT_DEFINITION_REF);
		this.RuleExecutableObjectName = ObjMap.get(EXECUTABLE_OBJECT_NAME_REF);
		this.RuleExecutableObjectAlias = ObjMap.get(EXECUTABLE_ALIAS);
		
//		[0_VALUE:1]
//		[0_OPERATOR:>]
//		[0_ATTRIBUTE:event.count]
		
//		[1_VALUE:true]
//		[1_OPERATOR:==]
//		[1_ATTRIBUTE:event.success]
		
//		[VALUE_MAPPING_0_event.count:MYCOUNT#]
//		[VALUE_MAPPING_1_event.message:MYMESSAGE#]
//		[VALUE_MAPPING_2_event.success:MYSUCCESS1#]

		ArrayList<String> AllRawConditions = new ArrayList<String>();
		ArrayList<String> AllRawMappings = new ArrayList<String>();
		
		Iterator<String> KeyIterator = Keys.iterator();
		
		while(KeyIterator.hasNext()) {
			String Key = KeyIterator.next();
			if(Key.matches("[0-9]+_.+")) {AllRawConditions.add(Key);}
			if(Key.startsWith("VALUE_MAPPING_")) {AllRawMappings.add(Key);}
		}


		for(int m = 0;m<AllRawMappings.size();m++){
			String Mapping = AllRawMappings.get(m);

			String AttrName = Mapping.split("VALUE_MAPPING_")[1].substring(2);
			IAVarMapping mapping = new IAVarMapping(Mapping,AttrName,ObjMap.get(Mapping));
			Mappings.add(mapping);
			//mapping.showMapping();
		}
		
		// Processing Conditions
		if(AllRawConditions.size() % 3 == 0) {
			int NumberOfRawObjects = AllRawConditions.size()/3;
			for(int j=0;j<NumberOfRawObjects;j++) {
				ArrayList<String> TempHolder = new ArrayList<String>();
				
				for(int k =0; k < AllRawConditions.size();k++) {
					if(AllRawConditions.get(k).startsWith(Integer.toString(j)+"_")) {
						TempHolder.add(AllRawConditions.get(k));
					}
				}
				IACondition cond = new IACondition(j);
				for(int l=0;l<TempHolder.size();l++) {
					String TempKey = TempHolder.get(l);
					if(TempKey.contains("VALUE")) {cond.setValue(ObjMap.get(TempKey));}
					if(TempKey.contains("OPERATOR")) {cond.setOperator(ObjMap.get(TempKey));}
					if(TempKey.contains("ATTRIBUTE")) {cond.setAttr(ObjMap.get(TempKey));}
					
				}
				Conditions.add(cond);
				//cond.showCondition();
				//cond.showKeys();
			}
		}else {
			//Error! 
		}

//		System.out.println("DEBUG:" +Conditions.size() );
//		System.out.println("DEBUG:" +Mappings.size() );
//		
	}
	
	public void showConditions() {
		System.out.println("\t Event Definition => ["+this.RuleSettingsVARA+"]");
		
		for( int i=0;i<this.Conditions.size();i++) {
			IACondition cond = this.Conditions.get(i);
			cond.showCondition();
		}
	}
	
	public void showActions() {
		System.out.println("\t Event Action [Name - Alias] => ["+this.RuleExecutableObjectName + " - "+RuleExecutableObjectAlias+"]");
		for( int i=0;i<this.Mappings.size();i++) {
			IAVarMapping cond = this.Mappings.get(i);
			cond.showMapping();
		}
	}
	
	// should prob check for attr format and variable format?
	public void addMapping(String Attr, String VariableName) {
		int Index = this.Mappings.size()+1;
		String XMLKey = "VALUE_MAPPING_"+Index+"_"+Attr;
		IAVarMapping mapping = new IAVarMapping(XMLKey,Attr,VariableName);
		this.Mappings.add(mapping);
		this.event.settings().getEventKeyValues().put(XMLKey, VariableName);
	}
	
	// should prob check for attr format and variable format?
	public void addCondition(String Attr,String Operator, String Value) {
		int Index = this.Conditions.size()+1;
		String XMLKeyValue = Index+"_VALUE";
		String XMLKeyOperator = Index+"_OPERATOR";
		String XMLKeyAttribute = Index+"_ATTRIBUTE";
		IACondition cond = new IACondition(Index);
		this.Conditions.add(cond);
		this.event.settings().getEventKeyValues().put(XMLKeyValue, Value);
		this.event.settings().getEventKeyValues().put(XMLKeyOperator, Operator);
		this.event.settings().getEventKeyValues().put(XMLKeyAttribute, Attr);
	}
	
	public class IACondition {
		int OriginalXMLKey = -1;
		String Operator = "";
		String Value = "";
		String Attribute = "";
		public IACondition(int XMLKey) {
			this.OriginalXMLKey = XMLKey;
		}
		public void setAttr(String Attr) {this.Attribute = Attr;}
		public void setOperator(String Operator) {this.Operator = Operator;}
		public void setValue(String Value) {this.Value = Value;}
		
		public void showCondition() {
			System.out.println("\t\t Condition (INDEX["+OriginalXMLKey+"]) => if "+ Attribute +" "+ Operator +" "+ Value);
		}
		
		public void showKeys() {
			System.out.println("\t\t => KEYS:[ " + OriginalXMLKey+"_ATTRIBUTE"+" : "+OriginalXMLKey+"_OPERATOR"+" : "+OriginalXMLKey+"_VALUE ]");
		}
		
		public String getAttributeKey() {return OriginalXMLKey+"_ATTRIBUTE";}
		public String getOperatorKey() {return OriginalXMLKey+"_OPERATOR";}
		public String getValueKey() {return OriginalXMLKey+"_VALUE";}
		
	}
	
	public class IAVarMapping {
		String OriginalXMLKey = "";
		String VariableName = "";
		String Attribute = "";
		public IAVarMapping(String XMLKey, String Attr, String VariableName) {
			this.OriginalXMLKey = XMLKey;
			this.VariableName = VariableName;
			this.Attribute = Attr;
		}
		public void showMapping() {
			System.out.println("\t\t Variable Mapping (KEY["+OriginalXMLKey+"]) => "+ Attribute +" To: "+ VariableName);
		}
		public String getKey() {
			return OriginalXMLKey;
		}

	}
}

