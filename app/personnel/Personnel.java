package personnel;

import java.util.List;

import lombok.Data;

@Data
public class Personnel {
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String emailAddress;
	private String streetAddress;
	private String city;
	private String state;
	private int zipCode;
	private List<Integer> certificationsIds;
	
	public void addCert(int certId)
	{
		if(!certificationsIds.contains(certId))
		{
			certificationsIds.add(certId);
		}
	}
}
