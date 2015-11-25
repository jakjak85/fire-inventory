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
			String msg = (String) message;
			Object obj = JSONValue.parse(msg);
			JSONObject jObj = (JSONObject) obj;
			Object msgTypeObj = jObj.get("type");
			String msgType = (String) msgTypeObj;
			MsgType type = Enum.valueOf(MsgType.class, msgType);
			determineAction(jObj, type);
		}
	}

	private void determineAction(JSONObject msg, MsgType type) {
		switch (type) {
		case UPDATE:
			updateAction(msg);
			break;
		case ADD:
			addAction(msg);
			break;
		case REMOVE:
			removeAction(msg);
			break;
		case QUERY:
			queryAction(msg);
			break;
		case QUERYID:
			queryIdAction(msg);
			break;
		default:
			break;
		}
	}

	private enum MsgType {
		UPDATE, ADD, REMOVE, QUERY, QUERYID
	}

	private void queryAction(JSONObject msg) {
		Object objFName = msg.get("firstName");
		Object objLName = msg.get("lastName");
		String fname = (String) objFName;
		String lname = (String) objLName;
		out.tell(accessor.getRecord(fname, lname), self());
	}
	
	private void queryIdAction(JSONObject msg) {
		Object objId = msg.get("id");
		String id = (String) objId;
		out.tell(accessor.getRecordById(id), self());
	}
	
	private void addAction(JSONObject msg) {
		
	}
	private void removeAction(JSONObject msg) {
		
	}
	private void updateAction(JSONObject msg) {
		
	}
}
