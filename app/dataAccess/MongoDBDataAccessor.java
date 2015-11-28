package dataAccess;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import play.Logger;

public final class MongoDBDataAccessor implements DataAccessor {

	private static final String SERVER = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "fireHouse";
	private static final String PERSONNEL = "people";

	private static DataAccessor instance = null;

	private MongoClient mongoClient;
	private MongoDatabase mongoDB;

	private MongoDBDataAccessor() {
		mongoClient = new MongoClient(SERVER, PORT);
		mongoDB = mongoClient.getDatabase(DATABASE);
	}

	public static synchronized DataAccessor getAccessor() {
		if (instance == null) {
			instance = new MongoDBDataAccessor();
		}
		return instance;
	}

	@Override
	public void updateRecord(JSONObject person) {
		try {
			mongoDB.getCollection(PERSONNEL).replaceOne(getPrimaryKeyDocument(person), toDocument(person));
		} catch (Exception e) {
			Logger.info("Error Updating", e);
		}
	}

	@Override
	public boolean removeUser(String id) {
		try {
			Document doc = new Document("_id", new ObjectId(id));
			DeleteResult result = mongoDB.getCollection(PERSONNEL).deleteOne(doc);
			if (result.getDeletedCount() == 1) {
				return true;
			}
			return false;
		} catch (Exception e) {
			Logger.info("Error removing", e);
			return false;
		}
	}

	@Override
	public void addRecord(JSONObject person) {
		try {
			person.remove("_id");
			mongoDB.getCollection(PERSONNEL).insertOne(toDocument(person));
		} catch (Exception e) {
			Logger.info("Error adding", e);
		}
	}

	@Override
	public String getRecordById(String id) {
		try {
			FindIterable<Document> iterable = mongoDB.getCollection(PERSONNEL)
					.find(new Document("_id", new ObjectId(id)));
			Document doc = iterable.first();
			if (doc != null) {
				return toJSONObject(doc).toJSONString();
			}
			return null;
		} catch (Exception e) {
			Logger.info("Error getting by id", e);
			return null;
		}
	}

	@Override
	public String getRecord(String firstName, String lastName) {
		try {
			Document doc = mongoDB.getCollection(PERSONNEL)
					.find(new Document("firstName", firstName).append("lastName", lastName)).first();
			if (doc != null) {
				return toJSONObject(doc).toJSONString();
			}
			return null;
		} catch (Exception e) {
			Logger.info("Error getting by name", e);
			return null;
		}
	}

	@Override
	public String getAnyRecord() {
		try {
			Document doc = mongoDB.getCollection(PERSONNEL).find().first();
			if (doc != null) {
				return toJSONObject(doc).toJSONString();
			}
			return null;
		} catch (Exception e) {
			Logger.info("Error getting by name", e);
			return null;
		}
	}

	private static Document toDocument(JSONObject person) {
		Document retVal = new Document();
		if (person.get("_id") != null) {
			retVal.append("_id", new ObjectId((String) person.get("_id")));
		}
		retVal.append("city", (String) person.get("city"));
		retVal.append("firstName", (String) person.get("firstName"));
		retVal.append("lastName", (String) person.get("lastName"));
		retVal.append("phoneNumber", (String) person.get("phoneNumber"));
		retVal.append("emailAddress", (String) person.get("emailAddress"));
		retVal.append("streetAddress", (String) person.get("streetAddress"));
		retVal.append("state", (String) person.get("state"));
		retVal.append("zipCode", (String) person.get("zipCode"));
		return retVal;
	}

	private static Document getPrimaryKeyDocument(JSONObject person) {
		Document retVal = new Document();
		if (person.get("_id") != null) {
			retVal.append("_id", new ObjectId((String) person.get("_id")));
		} else {
			retVal.append("firstName", (String) person.get("firstName"));
			retVal.append("lastName", (String) person.get("lastName"));
		}
		return retVal;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	private static JSONObject toJSONObject(Document doc) {
		JSONObject retVal = new JSONObject();
		retVal.put("_id", doc.get("_id").toString());
		retVal.put("city", doc.getString("city"));
		retVal.put("firstName", doc.getString("firstName"));
		retVal.put("lastName", doc.getString("lastName"));
		retVal.put("phoneNumber", doc.getString("phoneNumber"));
		retVal.put("emailAddress", doc.getString("emailAddress"));
		retVal.put("streetAddress", doc.getString("streetAddress"));
		retVal.put("state", doc.getString("state"));
		retVal.put("zipCode", doc.getString("zipCode"));
		return retVal;
	}
}