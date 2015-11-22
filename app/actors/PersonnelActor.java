package actors;

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
	        	out.tell(accessor.getRecord("John", "Doe"), self());
	        }
	    }
}


