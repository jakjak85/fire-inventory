package actors;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import actors.PersonnelActor.DetectChange;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import dataAccess.DataAccessor;
import dataAccess.MongoDBDataAccessor;
import play.Logger;

public class PersonnelActor extends UntypedActor {
	public static Props props(ActorRef out) {
		return Props.create(PersonnelActor.class, out);
	}

	private final ActorRef out;

	private DataAccessor accessor = MongoDBDataAccessor.getAccessor();
	private DetectChange changeWatcher;

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
		case QUERYANY:
			queryAnyAction();
			break;
		default:
			break;
		}
	}

	private enum MsgType {
		UPDATE, ADD, REMOVE, QUERY, QUERYID, QUERYANY
	}

	private void queryAnyAction() {
		String result = accessor.getAnyRecord();
		Object obj = JSONValue.parse(result);
		JSONObject jObj = (JSONObject) obj;
		String id = getProperty(jObj, "_id");
		if (result != null) {
			out.tell(result, self());

		} else {
			out.tell("{}", self());
		}
		startWatch(id, result);
	}

	private void queryAction(JSONObject msg) {
		String fname = getProperty(msg, "firstName");
		String lname = getProperty(msg, "lastName");
		String result = accessor.getRecord(fname, lname);
		Object obj = JSONValue.parse(result);
		JSONObject jObj = (JSONObject) obj;
		String id = getProperty(jObj, "_id");
		if (result != null) {
			out.tell(result, self());
		} else {
			out.tell("{}", self());
		}
		startWatch(id, result);
	}

	private void queryIdAction(JSONObject msg) {
		String id = getProperty(msg, "_id");
		String result = accessor.getRecordById(id);
		if (result != null) {
			out.tell(result, self());
		} else {
			out.tell("{}", self());
		}
		startWatch(id, result);
	}

	private void addAction(JSONObject msg) {
		accessor.addRecord(msg);
		out.tell("{}", self());
		stopWatch();
	}

	private void removeAction(JSONObject msg) {
		String id = getProperty(msg, "_id");
		accessor.removeUser(id);
		out.tell("{}", self());
		stopWatch();
	}

	private void updateAction(JSONObject msg) {
		accessor.updateRecord(msg);
	}

	private String getProperty(JSONObject msg, String propId) {
		Object obj = msg.get(propId);
		String str = (String) obj;
		return str;
	}

	private void startWatch(String id, String object) {
		if (changeWatcher != null) {
			stopWatch();
		}
		new Thread(new DetectChange(id, object)).start();
	}

	private void stopWatch() {
		if (changeWatcher != null) {
			changeWatcher.stop();
			changeWatcher = null;
		}
	}

	class DetectChange implements Runnable {
		String objectWatch = "";
		String objectId = "";
		volatile boolean keepGoing = true;

		DetectChange(String id, String object) {
			objectId = id;
			objectWatch = object;
		}

		public void run() {
			while (keepGoing) {
				try {
					Thread.sleep(1000);
					String newObject = accessor.getRecordById(objectId);
					if (!objectWatch.equals(newObject)) {
						out.tell(newObject, self());
						objectWatch = newObject;
					}
				} catch (Exception e) {
					Logger.error("Error sleeping", e);
				}
			}
		}

		public void stop() {
			keepGoing = false;
		}
	}
}
