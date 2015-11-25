package dataAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

public final class MongoDBAccessor {

	private static final String SERVER = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "washies";
	private static final String PERSONNEL = "people";

	private static MongoDBAccessor instance = null;

/*	private MongoClient mongoClient;
	private MongoDatabase mongoDB;*/
	private List<JSONObject> myDb = new ArrayList<JSONObject>();
	
	private MongoDBAccessor()
	{
		myDb.add(getTestPerson("1", "John", "Doe"));
		myDb.add(getTestPerson("2", "Peter", "TableMaker"));
		myDb.add(getTestPerson("3", "Joe", "Smith"));
		//mongoClient = new MongoClient(SERVER, PORT);
		//mongoDB = mongoClient.getDatabase(DATABASE);

	}
	private JSONObject getTestPerson(String id, String fname, String lname)
	{
		JSONObject cert1 = new JSONObject();
		cert1.put("id", "1");
		cert1.put("name", "FireFighter I");
		JSONObject cert2 = new JSONObject();
		cert2.put("id", "2");
		cert2.put("name", "FireFighter II");
		JSONObject obj = new JSONObject();
		obj.put("id", id);
		obj.put("city", "Conshohocken");
		obj.put("firstName", fname);
		obj.put("lastName", lname);
		obj.put("phoneNumber", "321-654-1425");
		obj.put("emailAddress", "213@abc.com");
		obj.put("streetAddress","34 street place" );
		obj.put("state","CA" );
		obj.put("zipCode", "12345");
		obj.put("certificationsIds", Arrays.asList(cert1, cert2));
		return obj;
	}
	
	public static synchronized MongoDBAccessor getAccessor() {
		if (instance == null) {
			instance = new MongoDBAccessor();
		}
		return instance;
	}

	public void updateRecord(JSONObject person) {
		String id = (String) person.get("id");
		for (JSONObject p : myDb)
		{
			if(p.get("id").equals(id))
			{
				p = person;
			}
		}
	}

	public void addRecord(JSONObject person) {

		// mongoDB.getCollection(PERSONNEL).insertOne(
		// new Document().append("Fname", value)
		if(getRecordById((String)person.get("id")) == null)
		{
			myDb.add(person);
		}
	}
	
	public String getRecordById(String id) {
		for (JSONObject p : myDb)
		{
			if(p.get("id").equals(id))
			{
				return p.toJSONString();
			}
		}
		return null;
	}
	
	public String getRecord(String firstName, String lastName) {
		for (JSONObject p : myDb)
		{
			if(p.get("firstName").equals(firstName) && p.get("lastName").equals(lastName))
			{
				return p.toJSONString();
			}
		}
		return null;
	}
}
