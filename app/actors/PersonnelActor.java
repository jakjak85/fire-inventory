package actors;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import dataAccess.MongoDBAccessor;

public class PersonnelActor extends UntypedActor {
	 public static Props props(ActorRef out) {
	        return Props.create(PersonnelActor.class, out);
	    }

	    private final ActorRef out;
	    
		private MongoDBAccessor accessor = MongoDBAccessor.getAccessor();
		
	    public PersonnelActor(ActorRef out) {
	        this.out = out;
	    }

	    public void onReceive(Object message) throws Exception {	    	
	        if (message instanceof String) {
	        	String msg = (String)message;
	        	Object obj = JSONValue.parse(msg);
	        	JSONObject jObj = (JSONObject)obj;
	        	Object objFName = jObj.get("firstName");
	        	Object objLName = jObj.get("lastName");
	        	String fname = (String)objFName;
	        	String lname = (String)objLName;
	        	out.tell(accessor.getRecord(fname, lname), self());
	        }
	    }
}


