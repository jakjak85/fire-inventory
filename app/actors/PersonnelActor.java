package actors;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import dataAccess.DataAccessor;
import dataAccess.SimpleDataAccessor;

public class PersonnelActor extends UntypedActor {
	public static Props props(ActorRef out) {
		return Props.create(PersonnelActor.class, out);
	}

	private final ActorRef out;

	private DataAccessor accessor = SimpleDataAccessor.getAccessor();

	public PersonnelActor(ActorRef out) {
		this.out = out;
	}

	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String) message;
			Object obj = JSONValue.parse(msg);
			JSONObject jObj = (JSONObject) obj;
			String msgType = getProperty(jObj, "type");
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
		String fname = getProperty(msg, "firstName");
		String lname = getProperty(msg, "lastName");
		String result = accessor.getRecord(fname, lname);
		if(result != null)
		{
			out.tell(result, self());
		}
		else
		{
			out.tell("{}", self());
		}
	}
	
	private void queryIdAction(JSONObject msg) {
		String id = getProperty(msg, "id");
		String result = accessor.getRecordById(id);
		if(result != null)
		{
			out.tell(result, self());
		}
		else
		{
			out.tell("{}", self());
		}
	}
	
	private void addAction(JSONObject msg) {
		accessor.addRecord(msg);
	}
	private void removeAction(JSONObject msg) {
		String id = getProperty(msg, "id");
		accessor.removeUser(id);
		out.tell("{}", self());
	}
	
	private void updateAction(JSONObject msg) {
		
		accessor.updateRecord(msg);
	}
	
	private String getProperty(JSONObject msg, String propId)
	{
		Object obj = msg.get(propId);
		String str = (String) obj;
		return str;
	}
}
