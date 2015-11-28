package dataAccess;

import org.json.simple.JSONObject;

public interface DataAccessor {

	void updateRecord(JSONObject person);

	boolean removeUser(String id);

	void addRecord(JSONObject person);

	String getRecordById(String id);

	String getRecord(String firstName, String lastName);

	String getAnyRecord();
}