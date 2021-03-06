package dataAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

public final class SimpleDataAccessor implements DataAccessor {

	private static int userId;

	private static DataAccessor instance = null;

	private List<JSONObject> myDb = new ArrayList<JSONObject>();

	private SimpleDataAccessor() {
		/*
		 * myDb.add(getTestPerson("1", "John", "Doe"));
		 * myDb.add(getTestPerson("2", "Peter", "TableMaker"));
		 * myDb.add(getTestPerson("3", "Joe", "Smith"));
		 */
		userId = 4;

	}

	private JSONObject getTestPerson(String id, String fname, String lname) {
		JSONObject cert1 = new JSONObject();
		cert1.put("_id", "1");
		cert1.put("name", "FireFighter I");
		JSONObject cert2 = new JSONObject();
		cert2.put("_id", "2");
		cert2.put("name", "FireFighter II");
		JSONObject obj = new JSONObject();
		obj.put("_id", id);
		obj.put("city", "Conshohocken");
		obj.put("firstName", fname);
		obj.put("lastName", lname);
		obj.put("phoneNumber", "321-654-1425");
		obj.put("emailAddress", "213@abc.com");
		obj.put("streetAddress", "34 street place");
		obj.put("state", "CA");
		obj.put("zipCode", "12345");
		obj.put("certificationsIds", Arrays.asList(cert1, cert2));
		return obj;
	}

	public static synchronized DataAccessor getAccessor() {
		if (instance == null) {
			instance = new SimpleDataAccessor();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dataAccess.DataAccessor#updateRecord(org.json.simple.JSONObject)
	 */
	@Override
	public void updateRecord(JSONObject person) {
		String id = (String) person.get("_id");
		if (removeUser(id)) {
			addRecord(person);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dataAccess.DataAccessor#removeUser(java.lang.String)
	 */
	@Override
	public boolean removeUser(String id) {
		JSONObject itemToRemove = null;
		for (JSONObject p : myDb) {
			if (p.get("_id").equals(id)) {
				itemToRemove = p;
				break;
			}
		}
		if (itemToRemove != null) {
			myDb.remove(itemToRemove);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dataAccess.DataAccessor#addRecord(org.json.simple.JSONObject)
	 */
	@Override
	public void addRecord(JSONObject person) {

		// mongoDB.getCollection(PERSONNEL).insertOne(
		// new Document().append("Fname", value)
		if (getRecordById((String) person.get("_id")) == null) {
			person.put("_id", String.valueOf(userId));
			userId++;
			myDb.add(person);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dataAccess.DataAccessor#getRecordById(java.lang.String)
	 */
	@Override
	public String getRecordById(String id) {
		for (JSONObject p : myDb) {
			if (p.get("_id").equals(id)) {
				return p.toJSONString();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dataAccess.DataAccessor#getRecord(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getRecord(String firstName, String lastName) {
		for (JSONObject p : myDb) {
			if (p.get("firstName").equals(firstName) && p.get("lastName").equals(lastName)) {
				return p.toJSONString();
			}
		}
		return null;
	}

	@Override
	public String getAnyRecord() {
		Iterator<JSONObject> iter = myDb.iterator();
		if (iter.hasNext()) {
			return iter.next().toJSONString();
		}
		return null;
	}
}
