package dataAccess;

import java.util.Arrays;

import org.json.simple.JSONObject;

import personnel.Personnel;

public final class MongoDBAccessor {

	private static final String SERVER = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "washies";
	private static final String PERSONNEL = "people";

	private static MongoDBAccessor instance = null;

/*	private MongoClient mongoClient;
	private MongoDatabase mongoDB;*/
	private Personnel rec;
	private JSONObject obj;
	
	private MongoDBAccessor()
	{
		JSONObject obj = new JSONObject();
		obj.put("city", "Conshohocken");
		obj.put("firstName", "John");
		obj.put("lastName", "doe");
		obj.put("phoneNumber", "321-654-1425");
		obj.put("emailAddress", "213@abc.com");
		obj.put("streetAddress","34 street place" );
		obj.put("state","CA" );
		obj.put("zipCode", "12345");
		obj.put("certificationsIds", Arrays.asList("123", "321"));
		//mongoClient = new MongoClient(SERVER, PORT);
		//mongoDB = mongoClient.getDatabase(DATABASE);
		rec = new Personnel();
		rec.setCity("Conshohocken");
		rec.setEmailAddress("213@abc.com");
		rec.setFirstName("John");
		rec.setLastName("doe");
		rec.setPhoneNumber("321-654-1425");
		rec.setState("CA");
		rec.setZipCode(12345);
		rec.setStreetAddress("34 street place");
	}

	public static synchronized MongoDBAccessor getAccessor() {
		if (instance == null) {
			instance = new MongoDBAccessor();
		}
		return instance;
	}

	public void updateRecord(Personnel person) {

		// mongoDB.getCollection(PERSONNEL).insertOne(
		// new Document().append("Fname", value)
	}

	public Personnel getClassRecord(String firstName, String LastName) {
		return rec;
	}
	
	public String getRecord(String firstName, String LastName) {
		return obj.toJSONString();
	}
}
